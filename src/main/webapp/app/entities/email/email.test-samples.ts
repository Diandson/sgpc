import dayjs from 'dayjs/esm';

import { IEmail, NewEmail } from './email.model';

export const sampleWithRequiredData: IEmail = {
  id: 6679,
};

export const sampleWithPartialData: IEmail = {
  id: 12633,
  objet: 'à la faveur de électorat outre',
  destinataire: 'combiner quant à',
  dateEnvoi: dayjs('2023-12-03T20:09'),
};

export const sampleWithFullData: IEmail = {
  id: 29749,
  objet: 'de façon que',
  contenu: 'snif',
  destinataire: 'spécialiste',
  dateEnvoi: dayjs('2023-12-03T15:56'),
};

export const sampleWithNewData: NewEmail = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
