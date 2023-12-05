import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IFiliale } from 'app/entities/filiale/filiale.model';
import { FilialeService } from 'app/entities/filiale/service/filiale.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPersonne } from '../personne.model';
import { PersonneService } from '../service/personne.service';
import { PersonneFormService } from './personne-form.service';

import { PersonneUpdateComponent } from './personne-update.component';

describe('Personne Management Update Component', () => {
  let comp: PersonneUpdateComponent;
  let fixture: ComponentFixture<PersonneUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let personneFormService: PersonneFormService;
  let personneService: PersonneService;
  let filialeService: FilialeService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PersonneUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PersonneUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PersonneUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    personneFormService = TestBed.inject(PersonneFormService);
    personneService = TestBed.inject(PersonneService);
    filialeService = TestBed.inject(FilialeService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Filiale query and add missing value', () => {
      const personne: IPersonne = { id: 456 };
      const filiale: IFiliale = { id: 7432 };
      personne.filiale = filiale;

      const filialeCollection: IFiliale[] = [{ id: 12296 }];
      jest.spyOn(filialeService, 'query').mockReturnValue(of(new HttpResponse({ body: filialeCollection })));
      const additionalFiliales = [filiale];
      const expectedCollection: IFiliale[] = [...additionalFiliales, ...filialeCollection];
      jest.spyOn(filialeService, 'addFilialeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ personne });
      comp.ngOnInit();

      expect(filialeService.query).toHaveBeenCalled();
      expect(filialeService.addFilialeToCollectionIfMissing).toHaveBeenCalledWith(
        filialeCollection,
        ...additionalFiliales.map(expect.objectContaining),
      );
      expect(comp.filialesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const personne: IPersonne = { id: 456 };
      const user: IUser = { id: 29767 };
      personne.user = user;

      const userCollection: IUser[] = [{ id: 10905 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ personne });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const personne: IPersonne = { id: 456 };
      const filiale: IFiliale = { id: 21694 };
      personne.filiale = filiale;
      const user: IUser = { id: 1088 };
      personne.user = user;

      activatedRoute.data = of({ personne });
      comp.ngOnInit();

      expect(comp.filialesSharedCollection).toContain(filiale);
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.personne).toEqual(personne);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPersonne>>();
      const personne = { id: 123 };
      jest.spyOn(personneFormService, 'getPersonne').mockReturnValue(personne);
      jest.spyOn(personneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: personne }));
      saveSubject.complete();

      // THEN
      expect(personneFormService.getPersonne).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(personneService.update).toHaveBeenCalledWith(expect.objectContaining(personne));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPersonne>>();
      const personne = { id: 123 };
      jest.spyOn(personneFormService, 'getPersonne').mockReturnValue({ id: null });
      jest.spyOn(personneService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personne: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: personne }));
      saveSubject.complete();

      // THEN
      expect(personneFormService.getPersonne).toHaveBeenCalled();
      expect(personneService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPersonne>>();
      const personne = { id: 123 };
      jest.spyOn(personneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(personneService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFiliale', () => {
      it('Should forward to filialeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(filialeService, 'compareFiliale');
        comp.compareFiliale(entity, entity2);
        expect(filialeService.compareFiliale).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
