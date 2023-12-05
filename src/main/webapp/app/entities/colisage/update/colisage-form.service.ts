import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IColisage, NewColisage } from '../colisage.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IColisage for edit and NewColisageFormGroupInput for create.
 */
type ColisageFormGroupInput = IColisage | PartialWithRequiredKeyOf<NewColisage>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IColisage | NewColisage> = Omit<T, 'dateCreation'> & {
  dateCreation?: string | null;
};

type ColisageFormRawValue = FormValueOf<IColisage>;

type NewColisageFormRawValue = FormValueOf<NewColisage>;

type ColisageFormDefaults = Pick<NewColisage, 'id' | 'estRecu' | 'dateCreation'>;

type ColisageFormGroupContent = {
  id: FormControl<ColisageFormRawValue['id'] | NewColisage['id']>;
  destination: FormControl<ColisageFormRawValue['destination']>;
  canal: FormControl<ColisageFormRawValue['canal']>;
  recuPar: FormControl<ColisageFormRawValue['recuPar']>;
  estRecu: FormControl<ColisageFormRawValue['estRecu']>;
  dateCreation: FormControl<ColisageFormRawValue['dateCreation']>;
  personne: FormControl<ColisageFormRawValue['personne']>;
};

export type ColisageFormGroup = FormGroup<ColisageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ColisageFormService {
  createColisageFormGroup(colisage: ColisageFormGroupInput = { id: null }): ColisageFormGroup {
    const colisageRawValue = this.convertColisageToColisageRawValue({
      ...this.getFormDefaults(),
      ...colisage,
    });
    return new FormGroup<ColisageFormGroupContent>({
      id: new FormControl(
        { value: colisageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      destination: new FormControl(colisageRawValue.destination),
      canal: new FormControl(colisageRawValue.canal),
      recuPar: new FormControl(colisageRawValue.recuPar),
      estRecu: new FormControl(colisageRawValue.estRecu),
      dateCreation: new FormControl(colisageRawValue.dateCreation),
      personne: new FormControl(colisageRawValue.personne),
    });
  }

  getColisage(form: ColisageFormGroup): IColisage | NewColisage {
    return this.convertColisageRawValueToColisage(form.getRawValue() as ColisageFormRawValue | NewColisageFormRawValue);
  }

  resetForm(form: ColisageFormGroup, colisage: ColisageFormGroupInput): void {
    const colisageRawValue = this.convertColisageToColisageRawValue({ ...this.getFormDefaults(), ...colisage });
    form.reset(
      {
        ...colisageRawValue,
        id: { value: colisageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ColisageFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      estRecu: false,
      dateCreation: currentTime,
    };
  }

  private convertColisageRawValueToColisage(rawColisage: ColisageFormRawValue | NewColisageFormRawValue): IColisage | NewColisage {
    return {
      ...rawColisage,
      dateCreation: dayjs(rawColisage.dateCreation, DATE_TIME_FORMAT),
    };
  }

  private convertColisageToColisageRawValue(
    colisage: IColisage | (Partial<NewColisage> & ColisageFormDefaults),
  ): ColisageFormRawValue | PartialWithRequiredKeyOf<NewColisageFormRawValue> {
    return {
      ...colisage,
      dateCreation: colisage.dateCreation ? colisage.dateCreation.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
