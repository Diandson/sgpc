import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../filiale.test-samples';

import { FilialeFormService } from './filiale-form.service';

describe('Filiale Form Service', () => {
  let service: FilialeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FilialeFormService);
  });

  describe('Service methods', () => {
    describe('createFilialeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFilialeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            denomination: expect.any(Object),
            sigle: expect.any(Object),
          }),
        );
      });

      it('passing IFiliale should create a new form with FormGroup', () => {
        const formGroup = service.createFilialeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            denomination: expect.any(Object),
            sigle: expect.any(Object),
          }),
        );
      });
    });

    describe('getFiliale', () => {
      it('should return NewFiliale for default Filiale initial value', () => {
        const formGroup = service.createFilialeFormGroup(sampleWithNewData);

        const filiale = service.getFiliale(formGroup) as any;

        expect(filiale).toMatchObject(sampleWithNewData);
      });

      it('should return NewFiliale for empty Filiale initial value', () => {
        const formGroup = service.createFilialeFormGroup();

        const filiale = service.getFiliale(formGroup) as any;

        expect(filiale).toMatchObject({});
      });

      it('should return IFiliale', () => {
        const formGroup = service.createFilialeFormGroup(sampleWithRequiredData);

        const filiale = service.getFiliale(formGroup) as any;

        expect(filiale).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFiliale should not enable id FormControl', () => {
        const formGroup = service.createFilialeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFiliale should disable id FormControl', () => {
        const formGroup = service.createFilialeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
