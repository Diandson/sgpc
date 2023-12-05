import { IFiliale, NewFiliale } from './filiale.model';

export const sampleWithRequiredData: IFiliale = {
  id: 23301,
};

export const sampleWithPartialData: IFiliale = {
  id: 19189,
  denomination: 'dring chef de cuisine debout',
};

export const sampleWithFullData: IFiliale = {
  id: 11844,
  denomination: 'oups rapide à la faveur de',
  sigle: 'détester',
};

export const sampleWithNewData: NewFiliale = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
