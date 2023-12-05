import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IColisage } from 'app/entities/colisage/colisage.model';
import { ColisageService } from 'app/entities/colisage/service/colisage.service';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';
import { IEmail } from '../email.model';
import { EmailService } from '../service/email.service';
import { EmailFormService } from './email-form.service';

import { EmailUpdateComponent } from './email-update.component';

describe('Email Management Update Component', () => {
  let comp: EmailUpdateComponent;
  let fixture: ComponentFixture<EmailUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let emailFormService: EmailFormService;
  let emailService: EmailService;
  let colisageService: ColisageService;
  let personneService: PersonneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), EmailUpdateComponent],
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
      .overrideTemplate(EmailUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmailUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    emailFormService = TestBed.inject(EmailFormService);
    emailService = TestBed.inject(EmailService);
    colisageService = TestBed.inject(ColisageService);
    personneService = TestBed.inject(PersonneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Colisage query and add missing value', () => {
      const email: IEmail = { id: 456 };
      const colisage: IColisage = { id: 18532 };
      email.colisage = colisage;

      const colisageCollection: IColisage[] = [{ id: 3351 }];
      jest.spyOn(colisageService, 'query').mockReturnValue(of(new HttpResponse({ body: colisageCollection })));
      const additionalColisages = [colisage];
      const expectedCollection: IColisage[] = [...additionalColisages, ...colisageCollection];
      jest.spyOn(colisageService, 'addColisageToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ email });
      comp.ngOnInit();

      expect(colisageService.query).toHaveBeenCalled();
      expect(colisageService.addColisageToCollectionIfMissing).toHaveBeenCalledWith(
        colisageCollection,
        ...additionalColisages.map(expect.objectContaining),
      );
      expect(comp.colisagesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Personne query and add missing value', () => {
      const email: IEmail = { id: 456 };
      const personne: IPersonne = { id: 5657 };
      email.personne = personne;

      const personneCollection: IPersonne[] = [{ id: 16926 }];
      jest.spyOn(personneService, 'query').mockReturnValue(of(new HttpResponse({ body: personneCollection })));
      const additionalPersonnes = [personne];
      const expectedCollection: IPersonne[] = [...additionalPersonnes, ...personneCollection];
      jest.spyOn(personneService, 'addPersonneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ email });
      comp.ngOnInit();

      expect(personneService.query).toHaveBeenCalled();
      expect(personneService.addPersonneToCollectionIfMissing).toHaveBeenCalledWith(
        personneCollection,
        ...additionalPersonnes.map(expect.objectContaining),
      );
      expect(comp.personnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const email: IEmail = { id: 456 };
      const colisage: IColisage = { id: 27404 };
      email.colisage = colisage;
      const personne: IPersonne = { id: 20464 };
      email.personne = personne;

      activatedRoute.data = of({ email });
      comp.ngOnInit();

      expect(comp.colisagesSharedCollection).toContain(colisage);
      expect(comp.personnesSharedCollection).toContain(personne);
      expect(comp.email).toEqual(email);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmail>>();
      const email = { id: 123 };
      jest.spyOn(emailFormService, 'getEmail').mockReturnValue(email);
      jest.spyOn(emailService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ email });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: email }));
      saveSubject.complete();

      // THEN
      expect(emailFormService.getEmail).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(emailService.update).toHaveBeenCalledWith(expect.objectContaining(email));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmail>>();
      const email = { id: 123 };
      jest.spyOn(emailFormService, 'getEmail').mockReturnValue({ id: null });
      jest.spyOn(emailService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ email: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: email }));
      saveSubject.complete();

      // THEN
      expect(emailFormService.getEmail).toHaveBeenCalled();
      expect(emailService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmail>>();
      const email = { id: 123 };
      jest.spyOn(emailService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ email });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(emailService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareColisage', () => {
      it('Should forward to colisageService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(colisageService, 'compareColisage');
        comp.compareColisage(entity, entity2);
        expect(colisageService.compareColisage).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePersonne', () => {
      it('Should forward to personneService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(personneService, 'comparePersonne');
        comp.comparePersonne(entity, entity2);
        expect(personneService.comparePersonne).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
