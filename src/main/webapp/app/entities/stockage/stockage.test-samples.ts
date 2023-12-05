import dayjs from 'dayjs/esm';

import { IStockage, NewStockage } from './stockage.model';

export const sampleWithRequiredData: IStockage = {
  id: 8724,
};

export const sampleWithPartialData: IStockage = {
  id: 7348,
  denomination: 'adversaire',
  quantite: 'près de anéantir',
  dateCreation: dayjs('2023-12-04T02:11'),
};

export const sampleWithFullData: IStockage = {
  id: 24758,
  denomination: 'à défaut de  éliminer adversaire',
  code: 'tellement oups bè',
  quantite: 'derrière doucement',
  modifierPar: 'autrement',
  dateCreation: dayjs('2023-12-04T09:14'),
};

export const sampleWithNewData: NewStockage = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
