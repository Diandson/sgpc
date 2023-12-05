import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../stockage.test-samples';

import { StockageFormService } from './stockage-form.service';

describe('Stockage Form Service', () => {
  let service: StockageFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StockageFormService);
  });

  describe('Service methods', () => {
    describe('createStockageFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStockageFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            denomination: expect.any(Object),
            code: expect.any(Object),
            quantite: expect.any(Object),
            modifierPar: expect.any(Object),
            dateCreation: expect.any(Object),
            personne: expect.any(Object),
          }),
        );
      });

      it('passing IStockage should create a new form with FormGroup', () => {
        const formGroup = service.createStockageFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            denomination: expect.any(Object),
            code: expect.any(Object),
            quantite: expect.any(Object),
            modifierPar: expect.any(Object),
            dateCreation: expect.any(Object),
            personne: expect.any(Object),
          }),
        );
      });
    });

    describe('getStockage', () => {
      it('should return NewStockage for default Stockage initial value', () => {
        const formGroup = service.createStockageFormGroup(sampleWithNewData);

        const stockage = service.getStockage(formGroup) as any;

        expect(stockage).toMatchObject(sampleWithNewData);
      });

      it('should return NewStockage for empty Stockage initial value', () => {
        const formGroup = service.createStockageFormGroup();

        const stockage = service.getStockage(formGroup) as any;

        expect(stockage).toMatchObject({});
      });

      it('should return IStockage', () => {
        const formGroup = service.createStockageFormGroup(sampleWithRequiredData);

        const stockage = service.getStockage(formGroup) as any;

        expect(stockage).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStockage should not enable id FormControl', () => {
        const formGroup = service.createStockageFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStockage should disable id FormControl', () => {
        const formGroup = service.createStockageFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
