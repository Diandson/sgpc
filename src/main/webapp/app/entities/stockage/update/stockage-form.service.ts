import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IStockage, NewStockage } from '../stockage.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStockage for edit and NewStockageFormGroupInput for create.
 */
type StockageFormGroupInput = IStockage | PartialWithRequiredKeyOf<NewStockage>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IStockage | NewStockage> = Omit<T, 'dateCreation'> & {
  dateCreation?: string | null;
};

type StockageFormRawValue = FormValueOf<IStockage>;

type NewStockageFormRawValue = FormValueOf<NewStockage>;

type StockageFormDefaults = Pick<NewStockage, 'id' | 'dateCreation'>;

type StockageFormGroupContent = {
  id: FormControl<StockageFormRawValue['id'] | NewStockage['id']>;
  denomination: FormControl<StockageFormRawValue['denomination']>;
  code: FormControl<StockageFormRawValue['code']>;
  quantite: FormControl<StockageFormRawValue['quantite']>;
  modifierPar: FormControl<StockageFormRawValue['modifierPar']>;
  dateCreation: FormControl<StockageFormRawValue['dateCreation']>;
  personne: FormControl<StockageFormRawValue['personne']>;
};

export type StockageFormGroup = FormGroup<StockageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StockageFormService {
  createStockageFormGroup(stockage: StockageFormGroupInput = { id: null }): StockageFormGroup {
    const stockageRawValue = this.convertStockageToStockageRawValue({
      ...this.getFormDefaults(),
      ...stockage,
    });
    return new FormGroup<StockageFormGroupContent>({
      id: new FormControl(
        { value: stockageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      denomination: new FormControl(stockageRawValue.denomination),
      code: new FormControl(stockageRawValue.code),
      quantite: new FormControl(stockageRawValue.quantite),
      modifierPar: new FormControl(stockageRawValue.modifierPar),
      dateCreation: new FormControl(stockageRawValue.dateCreation),
      personne: new FormControl(stockageRawValue.personne),
    });
  }

  getStockage(form: StockageFormGroup): IStockage | NewStockage {
    return this.convertStockageRawValueToStockage(form.getRawValue() as StockageFormRawValue | NewStockageFormRawValue);
  }

  resetForm(form: StockageFormGroup, stockage: StockageFormGroupInput): void {
    const stockageRawValue = this.convertStockageToStockageRawValue({ ...this.getFormDefaults(), ...stockage });
    form.reset(
      {
        ...stockageRawValue,
        id: { value: stockageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): StockageFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreation: currentTime,
    };
  }

  private convertStockageRawValueToStockage(rawStockage: StockageFormRawValue | NewStockageFormRawValue): IStockage | NewStockage {
    return {
      ...rawStockage,
      dateCreation: dayjs(rawStockage.dateCreation, DATE_TIME_FORMAT),
    };
  }

  private convertStockageToStockageRawValue(
    stockage: IStockage | (Partial<NewStockage> & StockageFormDefaults),
  ): StockageFormRawValue | PartialWithRequiredKeyOf<NewStockageFormRawValue> {
    return {
      ...stockage,
      dateCreation: stockage.dateCreation ? stockage.dateCreation.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
