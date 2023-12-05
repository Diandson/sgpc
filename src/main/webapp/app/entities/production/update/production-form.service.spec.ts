import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../production.test-samples';

import { ProductionFormService } from './production-form.service';

describe('Production Form Service', () => {
  let service: ProductionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductionFormService);
  });

  describe('Service methods', () => {
    describe('createProductionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelle: expect.any(Object),
            fichier: expect.any(Object),
            finish: expect.any(Object),
            etat: expect.any(Object),
            validerPar: expect.any(Object),
            dateDepot: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            dateValider: expect.any(Object),
            dateOuvert: expect.any(Object),
            dateCreation: expect.any(Object),
            personne: expect.any(Object),
            producteur: expect.any(Object),
            receveur: expect.any(Object),
          }),
        );
      });

      it('passing IProduction should create a new form with FormGroup', () => {
        const formGroup = service.createProductionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelle: expect.any(Object),
            fichier: expect.any(Object),
            finish: expect.any(Object),
            etat: expect.any(Object),
            validerPar: expect.any(Object),
            dateDepot: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            dateValider: expect.any(Object),
            dateOuvert: expect.any(Object),
            dateCreation: expect.any(Object),
            personne: expect.any(Object),
            producteur: expect.any(Object),
            receveur: expect.any(Object),
          }),
        );
      });
    });

    describe('getProduction', () => {
      it('should return NewProduction for default Production initial value', () => {
        const formGroup = service.createProductionFormGroup(sampleWithNewData);

        const production = service.getProduction(formGroup) as any;

        expect(production).toMatchObject(sampleWithNewData);
      });

      it('should return NewProduction for empty Production initial value', () => {
        const formGroup = service.createProductionFormGroup();

        const production = service.getProduction(formGroup) as any;

        expect(production).toMatchObject({});
      });

      it('should return IProduction', () => {
        const formGroup = service.createProductionFormGroup(sampleWithRequiredData);

        const production = service.getProduction(formGroup) as any;

        expect(production).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProduction should not enable id FormControl', () => {
        const formGroup = service.createProductionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProduction should disable id FormControl', () => {
        const formGroup = service.createProductionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
