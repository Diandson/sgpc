import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEmail, NewEmail } from '../email.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmail for edit and NewEmailFormGroupInput for create.
 */
type EmailFormGroupInput = IEmail | PartialWithRequiredKeyOf<NewEmail>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEmail | NewEmail> = Omit<T, 'dateEnvoi'> & {
  dateEnvoi?: string | null;
};

type EmailFormRawValue = FormValueOf<IEmail>;

type NewEmailFormRawValue = FormValueOf<NewEmail>;

type EmailFormDefaults = Pick<NewEmail, 'id' | 'dateEnvoi'>;

type EmailFormGroupContent = {
  id: FormControl<EmailFormRawValue['id'] | NewEmail['id']>;
  objet: FormControl<EmailFormRawValue['objet']>;
  contenu: FormControl<EmailFormRawValue['contenu']>;
  destinataire: FormControl<EmailFormRawValue['destinataire']>;
  dateEnvoi: FormControl<EmailFormRawValue['dateEnvoi']>;
  colisage: FormControl<EmailFormRawValue['colisage']>;
  personne: FormControl<EmailFormRawValue['personne']>;
};

export type EmailFormGroup = FormGroup<EmailFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmailFormService {
  createEmailFormGroup(email: EmailFormGroupInput = { id: null }): EmailFormGroup {
    const emailRawValue = this.convertEmailToEmailRawValue({
      ...this.getFormDefaults(),
      ...email,
    });
    return new FormGroup<EmailFormGroupContent>({
      id: new FormControl(
        { value: emailRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      objet: new FormControl(emailRawValue.objet),
      contenu: new FormControl(emailRawValue.contenu),
      destinataire: new FormControl(emailRawValue.destinataire),
      dateEnvoi: new FormControl(emailRawValue.dateEnvoi),
      colisage: new FormControl(emailRawValue.colisage),
      personne: new FormControl(emailRawValue.personne),
    });
  }

  getEmail(form: EmailFormGroup): IEmail | NewEmail {
    return this.convertEmailRawValueToEmail(form.getRawValue() as EmailFormRawValue | NewEmailFormRawValue);
  }

  resetForm(form: EmailFormGroup, email: EmailFormGroupInput): void {
    const emailRawValue = this.convertEmailToEmailRawValue({ ...this.getFormDefaults(), ...email });
    form.reset(
      {
        ...emailRawValue,
        id: { value: emailRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EmailFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateEnvoi: currentTime,
    };
  }

  private convertEmailRawValueToEmail(rawEmail: EmailFormRawValue | NewEmailFormRawValue): IEmail | NewEmail {
    return {
      ...rawEmail,
      dateEnvoi: dayjs(rawEmail.dateEnvoi, DATE_TIME_FORMAT),
    };
  }

  private convertEmailToEmailRawValue(
    email: IEmail | (Partial<NewEmail> & EmailFormDefaults),
  ): EmailFormRawValue | PartialWithRequiredKeyOf<NewEmailFormRawValue> {
    return {
      ...email,
      dateEnvoi: email.dateEnvoi ? email.dateEnvoi.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
