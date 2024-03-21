import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProduction, NewProduction } from '../production.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProduction for edit and NewProductionFormGroupInput for create.
 */
type ProductionFormGroupInput = IProduction | PartialWithRequiredKeyOf<NewProduction>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProduction | NewProduction> = Omit<T, 'dateValider' | 'dateCreation'> & {
  dateValider?: string | null;
  dateCreation?: string | null;
};

type ProductionFormRawValue = FormValueOf<IProduction>;

type NewProductionFormRawValue = FormValueOf<NewProduction>;

type ProductionFormDefaults = Pick<NewProduction, 'id' | 'finish' | 'dateValider' | 'dateCreation' | 'finished'>;

type ProductionFormGroupContent = {
  id: FormControl<ProductionFormRawValue['id'] | NewProduction['id']>;
  libelle: FormControl<ProductionFormRawValue['libelle']>;
  fichier: FormControl<ProductionFormRawValue['fichier']>;
  fichierContentType: FormControl<ProductionFormRawValue['fichierContentType']>;
  finish: FormControl<ProductionFormRawValue['finish']>;
  etat: FormControl<ProductionFormRawValue['etat']>;
  validerPar: FormControl<ProductionFormRawValue['validerPar']>;
  dateDepot: FormControl<ProductionFormRawValue['dateDepot']>;
  dateDebut: FormControl<ProductionFormRawValue['dateDebut']>;
  dateFin: FormControl<ProductionFormRawValue['dateFin']>;
  dateValider: FormControl<ProductionFormRawValue['dateValider']>;
  dateOuvert: FormControl<ProductionFormRawValue['dateOuvert']>;
  dateCreation: FormControl<ProductionFormRawValue['dateCreation']>;
  fichierControle: FormControl<ProductionFormRawValue['fichierControle']>;
  fichierControleContentType: FormControl<ProductionFormRawValue['fichierControleContentType']>;
  fichierReception: FormControl<ProductionFormRawValue['fichierReception']>;
  fichierReceptionContentType: FormControl<ProductionFormRawValue['fichierReceptionContentType']>;
  finished: FormControl<ProductionFormRawValue['finished']>;
  personne: FormControl<ProductionFormRawValue['personne']>;
  producteur: FormControl<ProductionFormRawValue['producteur']>;
  receveur: FormControl<ProductionFormRawValue['receveur']>;
  colisage: FormControl<ProductionFormRawValue['colisage']>;
};

export type ProductionFormGroup = FormGroup<ProductionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductionFormService {
  createProductionFormGroup(production: ProductionFormGroupInput = { id: null }): ProductionFormGroup {
    const productionRawValue = this.convertProductionToProductionRawValue({
      ...this.getFormDefaults(),
      ...production,
    });
    return new FormGroup<ProductionFormGroupContent>({
      id: new FormControl(
        { value: productionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelle: new FormControl(productionRawValue.libelle),
      fichier: new FormControl(productionRawValue.fichier),
      fichierContentType: new FormControl(productionRawValue.fichierContentType),
      finish: new FormControl(productionRawValue.finish),
      etat: new FormControl(productionRawValue.etat),
      validerPar: new FormControl(productionRawValue.validerPar),
      dateDepot: new FormControl(productionRawValue.dateDepot),
      dateDebut: new FormControl(productionRawValue.dateDebut),
      dateFin: new FormControl(productionRawValue.dateFin),
      dateValider: new FormControl(productionRawValue.dateValider),
      dateOuvert: new FormControl(productionRawValue.dateOuvert),
      dateCreation: new FormControl(productionRawValue.dateCreation),
      fichierControle: new FormControl(productionRawValue.fichierControle),
      fichierControleContentType: new FormControl(productionRawValue.fichierControleContentType),
      fichierReception: new FormControl(productionRawValue.fichierReception),
      fichierReceptionContentType: new FormControl(productionRawValue.fichierReceptionContentType),
      finished: new FormControl(productionRawValue.finished),
      personne: new FormControl(productionRawValue.personne),
      producteur: new FormControl(productionRawValue.producteur),
      receveur: new FormControl(productionRawValue.receveur),
      colisage: new FormControl(productionRawValue.colisage),
    });
  }

  getProduction(form: ProductionFormGroup): IProduction | NewProduction {
    return this.convertProductionRawValueToProduction(form.getRawValue() as ProductionFormRawValue | NewProductionFormRawValue);
  }

  resetForm(form: ProductionFormGroup, production: ProductionFormGroupInput): void {
    const productionRawValue = this.convertProductionToProductionRawValue({ ...this.getFormDefaults(), ...production });
    form.reset(
      {
        ...productionRawValue,
        id: { value: productionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProductionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      finish: false,
      dateCreation: currentTime,
      finished: false,
    };
  }

  private convertProductionRawValueToProduction(
    rawProduction: ProductionFormRawValue | NewProductionFormRawValue,
  ): IProduction | NewProduction {
    return {
      ...rawProduction,
      dateValider: dayjs(rawProduction.dateValider, DATE_TIME_FORMAT),
      dateCreation: dayjs(rawProduction.dateCreation, DATE_TIME_FORMAT),
    };
  }

  private convertProductionToProductionRawValue(
    production: IProduction | (Partial<NewProduction> & ProductionFormDefaults),
  ): ProductionFormRawValue | PartialWithRequiredKeyOf<NewProductionFormRawValue> {
    return {
      ...production,
      dateValider: production.dateValider ? production.dateValider.format(DATE_TIME_FORMAT) : undefined,
      dateCreation: production.dateCreation ? production.dateCreation.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
