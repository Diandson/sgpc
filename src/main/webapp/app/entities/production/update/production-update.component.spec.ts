import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';
import { ProductionService } from '../service/production.service';
import { IProduction } from '../production.model';
import { ProductionFormService } from './production-form.service';

import { ProductionUpdateComponent } from './production-update.component';

describe('Production Management Update Component', () => {
  let comp: ProductionUpdateComponent;
  let fixture: ComponentFixture<ProductionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productionFormService: ProductionFormService;
  let productionService: ProductionService;
  let personneService: PersonneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ProductionUpdateComponent],
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
      .overrideTemplate(ProductionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productionFormService = TestBed.inject(ProductionFormService);
    productionService = TestBed.inject(ProductionService);
    personneService = TestBed.inject(PersonneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Personne query and add missing value', () => {
      const production: IProduction = { id: 456 };
      const personne: IPersonne = { id: 17513 };
      production.personne = personne;
      const producteur: IPersonne = { id: 24628 };
      production.producteur = producteur;
      const receveur: IPersonne = { id: 16764 };
      production.receveur = receveur;

      const personneCollection: IPersonne[] = [{ id: 7378 }];
      jest.spyOn(personneService, 'query').mockReturnValue(of(new HttpResponse({ body: personneCollection })));
      const additionalPersonnes = [personne, producteur, receveur];
      const expectedCollection: IPersonne[] = [...additionalPersonnes, ...personneCollection];
      jest.spyOn(personneService, 'addPersonneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ production });
      comp.ngOnInit();

      expect(personneService.query).toHaveBeenCalled();
      expect(personneService.addPersonneToCollectionIfMissing).toHaveBeenCalledWith(
        personneCollection,
        ...additionalPersonnes.map(expect.objectContaining),
      );
      expect(comp.personnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const production: IProduction = { id: 456 };
      const personne: IPersonne = { id: 2862 };
      production.personne = personne;
      const producteur: IPersonne = { id: 20968 };
      production.producteur = producteur;
      const receveur: IPersonne = { id: 27623 };
      production.receveur = receveur;

      activatedRoute.data = of({ production });
      comp.ngOnInit();

      expect(comp.personnesSharedCollection).toContain(personne);
      expect(comp.personnesSharedCollection).toContain(producteur);
      expect(comp.personnesSharedCollection).toContain(receveur);
      expect(comp.production).toEqual(production);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduction>>();
      const production = { id: 123 };
      jest.spyOn(productionFormService, 'getProduction').mockReturnValue(production);
      jest.spyOn(productionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ production });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: production }));
      saveSubject.complete();

      // THEN
      expect(productionFormService.getProduction).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productionService.update).toHaveBeenCalledWith(expect.objectContaining(production));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduction>>();
      const production = { id: 123 };
      jest.spyOn(productionFormService, 'getProduction').mockReturnValue({ id: null });
      jest.spyOn(productionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ production: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: production }));
      saveSubject.complete();

      // THEN
      expect(productionFormService.getProduction).toHaveBeenCalled();
      expect(productionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduction>>();
      const production = { id: 123 };
      jest.spyOn(productionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ production });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productionService.update).toHaveBeenCalled();
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
