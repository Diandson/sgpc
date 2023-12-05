import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';
import { StockageService } from '../service/stockage.service';
import { IStockage } from '../stockage.model';
import { StockageFormService } from './stockage-form.service';

import { StockageUpdateComponent } from './stockage-update.component';

describe('Stockage Management Update Component', () => {
  let comp: StockageUpdateComponent;
  let fixture: ComponentFixture<StockageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let stockageFormService: StockageFormService;
  let stockageService: StockageService;
  let personneService: PersonneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), StockageUpdateComponent],
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
      .overrideTemplate(StockageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StockageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    stockageFormService = TestBed.inject(StockageFormService);
    stockageService = TestBed.inject(StockageService);
    personneService = TestBed.inject(PersonneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Personne query and add missing value', () => {
      const stockage: IStockage = { id: 456 };
      const personne: IPersonne = { id: 12381 };
      stockage.personne = personne;

      const personneCollection: IPersonne[] = [{ id: 28300 }];
      jest.spyOn(personneService, 'query').mockReturnValue(of(new HttpResponse({ body: personneCollection })));
      const additionalPersonnes = [personne];
      const expectedCollection: IPersonne[] = [...additionalPersonnes, ...personneCollection];
      jest.spyOn(personneService, 'addPersonneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ stockage });
      comp.ngOnInit();

      expect(personneService.query).toHaveBeenCalled();
      expect(personneService.addPersonneToCollectionIfMissing).toHaveBeenCalledWith(
        personneCollection,
        ...additionalPersonnes.map(expect.objectContaining),
      );
      expect(comp.personnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const stockage: IStockage = { id: 456 };
      const personne: IPersonne = { id: 6250 };
      stockage.personne = personne;

      activatedRoute.data = of({ stockage });
      comp.ngOnInit();

      expect(comp.personnesSharedCollection).toContain(personne);
      expect(comp.stockage).toEqual(stockage);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStockage>>();
      const stockage = { id: 123 };
      jest.spyOn(stockageFormService, 'getStockage').mockReturnValue(stockage);
      jest.spyOn(stockageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stockage }));
      saveSubject.complete();

      // THEN
      expect(stockageFormService.getStockage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(stockageService.update).toHaveBeenCalledWith(expect.objectContaining(stockage));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStockage>>();
      const stockage = { id: 123 };
      jest.spyOn(stockageFormService, 'getStockage').mockReturnValue({ id: null });
      jest.spyOn(stockageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockage: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stockage }));
      saveSubject.complete();

      // THEN
      expect(stockageFormService.getStockage).toHaveBeenCalled();
      expect(stockageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStockage>>();
      const stockage = { id: 123 };
      jest.spyOn(stockageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(stockageService.update).toHaveBeenCalled();
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
