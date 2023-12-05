import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../colisage.test-samples';

import { ColisageFormService } from './colisage-form.service';

describe('Colisage Form Service', () => {
  let service: ColisageFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ColisageFormService);
  });

  describe('Service methods', () => {
    describe('createColisageFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createColisageFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            destination: expect.any(Object),
            canal: expect.any(Object),
            recuPar: expect.any(Object),
            estRecu: expect.any(Object),
            dateCreation: expect.any(Object),
            personne: expect.any(Object),
          }),
        );
      });

      it('passing IColisage should create a new form with FormGroup', () => {
        const formGroup = service.createColisageFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            destination: expect.any(Object),
            canal: expect.any(Object),
            recuPar: expect.any(Object),
            estRecu: expect.any(Object),
            dateCreation: expect.any(Object),
            personne: expect.any(Object),
          }),
        );
      });
    });

    describe('getColisage', () => {
      it('should return NewColisage for default Colisage initial value', () => {
        const formGroup = service.createColisageFormGroup(sampleWithNewData);

        const colisage = service.getColisage(formGroup) as any;

        expect(colisage).toMatchObject(sampleWithNewData);
      });

      it('should return NewColisage for empty Colisage initial value', () => {
        const formGroup = service.createColisageFormGroup();

        const colisage = service.getColisage(formGroup) as any;

        expect(colisage).toMatchObject({});
      });

      it('should return IColisage', () => {
        const formGroup = service.createColisageFormGroup(sampleWithRequiredData);

        const colisage = service.getColisage(formGroup) as any;

        expect(colisage).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IColisage should not enable id FormControl', () => {
        const formGroup = service.createColisageFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewColisage should disable id FormControl', () => {
        const formGroup = service.createColisageFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
