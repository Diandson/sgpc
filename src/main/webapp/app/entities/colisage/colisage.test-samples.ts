import dayjs from 'dayjs/esm';

import { IColisage, NewColisage } from './colisage.model';

export const sampleWithRequiredData: IColisage = {
  id: 17217,
};

export const sampleWithPartialData: IColisage = {
  id: 19974,
  destination: 'sauvage',
  canal: 'membre de l’équipe',
};

export const sampleWithFullData: IColisage = {
  id: 6933,
  destination: 'plouf',
  canal: 'vivace',
  recuPar: 'conseil d’administration hâter',
  estRecu: true,
  dateCreation: dayjs('2023-12-03T16:20'),
};

export const sampleWithNewData: NewColisage = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
