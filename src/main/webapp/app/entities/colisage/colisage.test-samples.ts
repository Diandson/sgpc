import dayjs from 'dayjs/esm';

import { IColisage, NewColisage } from './colisage.model';

export const sampleWithRequiredData: IColisage = {
  id: 25829,
};

export const sampleWithPartialData: IColisage = {
  id: 12815,
  destination: 'cot cot',
  canal: 'assez',
  recuPar: 'hé',
  estRecu: false,
};

export const sampleWithFullData: IColisage = {
  id: 25033,
  destination: 'alors que prestataire de services',
  canal: 'bzzz proche',
  recuPar: 'pendre aigre mature',
  estRecu: true,
  dateCreation: dayjs('2023-12-04T06:50'),
};

export const sampleWithNewData: NewColisage = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
