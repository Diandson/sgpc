import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFiliale, NewFiliale } from '../filiale.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFiliale for edit and NewFilialeFormGroupInput for create.
 */
type FilialeFormGroupInput = IFiliale | PartialWithRequiredKeyOf<NewFiliale>;

type FilialeFormDefaults = Pick<NewFiliale, 'id'>;

type FilialeFormGroupContent = {
  id: FormControl<IFiliale['id'] | NewFiliale['id']>;
  denomination: FormControl<IFiliale['denomination']>;
  sigle: FormControl<IFiliale['sigle']>;
};

export type FilialeFormGroup = FormGroup<FilialeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FilialeFormService {
  createFilialeFormGroup(filiale: FilialeFormGroupInput = { id: null }): FilialeFormGroup {
    const filialeRawValue = {
      ...this.getFormDefaults(),
      ...filiale,
    };
    return new FormGroup<FilialeFormGroupContent>({
      id: new FormControl(
        { value: filialeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      denomination: new FormControl(filialeRawValue.denomination),
      sigle: new FormControl(filialeRawValue.sigle),
    });
  }

  getFiliale(form: FilialeFormGroup): IFiliale | NewFiliale {
    return form.getRawValue() as IFiliale | NewFiliale;
  }

  resetForm(form: FilialeFormGroup, filiale: FilialeFormGroupInput): void {
    const filialeRawValue = { ...this.getFormDefaults(), ...filiale };
    form.reset(
      {
        ...filialeRawValue,
        id: { value: filialeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FilialeFormDefaults {
    return {
      id: null,
    };
  }
}
