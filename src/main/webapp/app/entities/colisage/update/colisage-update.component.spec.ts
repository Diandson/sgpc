import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';
import { ColisageService } from '../service/colisage.service';
import { IColisage } from '../colisage.model';
import { ColisageFormService } from './colisage-form.service';

import { ColisageUpdateComponent } from './colisage-update.component';

describe('Colisage Management Update Component', () => {
  let comp: ColisageUpdateComponent;
  let fixture: ComponentFixture<ColisageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let colisageFormService: ColisageFormService;
  let colisageService: ColisageService;
  let personneService: PersonneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ColisageUpdateComponent],
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
      .overrideTemplate(ColisageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ColisageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    colisageFormService = TestBed.inject(ColisageFormService);
    colisageService = TestBed.inject(ColisageService);
    personneService = TestBed.inject(PersonneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Personne query and add missing value', () => {
      const colisage: IColisage = { id: 456 };
      const personne: IPersonne = { id: 19926 };
      colisage.personne = personne;

      const personneCollection: IPersonne[] = [{ id: 28518 }];
      jest.spyOn(personneService, 'query').mockReturnValue(of(new HttpResponse({ body: personneCollection })));
      const additionalPersonnes = [personne];
      const expectedCollection: IPersonne[] = [...additionalPersonnes, ...personneCollection];
      jest.spyOn(personneService, 'addPersonneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ colisage });
      comp.ngOnInit();

      expect(personneService.query).toHaveBeenCalled();
      expect(personneService.addPersonneToCollectionIfMissing).toHaveBeenCalledWith(
        personneCollection,
        ...additionalPersonnes.map(expect.objectContaining),
      );
      expect(comp.personnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const colisage: IColisage = { id: 456 };
      const personne: IPersonne = { id: 27687 };
      colisage.personne = personne;

      activatedRoute.data = of({ colisage });
      comp.ngOnInit();

      expect(comp.personnesSharedCollection).toContain(personne);
      expect(comp.colisage).toEqual(colisage);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IColisage>>();
      const colisage = { id: 123 };
      jest.spyOn(colisageFormService, 'getColisage').mockReturnValue(colisage);
      jest.spyOn(colisageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ colisage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: colisage }));
      saveSubject.complete();

      // THEN
      expect(colisageFormService.getColisage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(colisageService.update).toHaveBeenCalledWith(expect.objectContaining(colisage));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IColisage>>();
      const colisage = { id: 123 };
      jest.spyOn(colisageFormService, 'getColisage').mockReturnValue({ id: null });
      jest.spyOn(colisageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ colisage: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: colisage }));
      saveSubject.complete();

      // THEN
      expect(colisageFormService.getColisage).toHaveBeenCalled();
      expect(colisageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IColisage>>();
      const colisage = { id: 123 };
      jest.spyOn(colisageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ colisage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(colisageService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
