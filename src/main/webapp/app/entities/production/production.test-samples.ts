import dayjs from 'dayjs/esm';

import { IProduction, NewProduction } from './production.model';

export const sampleWithRequiredData: IProduction = {
  id: 18715,
};

export const sampleWithPartialData: IProduction = {
  id: 16965,
  fichier: '../fake-data/blob/hipster.png',
  fichierContentType: 'unknown',
  finish: false,
  etat: 'COURRIER',
  validerPar: 'sauf comme accumuler',
  dateDebut: dayjs('2023-12-04'),
  dateFin: dayjs('2023-12-04'),
  dateValider: dayjs('2023-12-03T16:46'),
};

export const sampleWithFullData: IProduction = {
  id: 31016,
  libelle: 'sous',
  fichier: '../fake-data/blob/hipster.png',
  fichierContentType: 'unknown',
  finish: true,
  etat: 'TERMINER',
  validerPar: 'spécialiste',
  dateDepot: dayjs('2023-12-03'),
  dateDebut: dayjs('2023-12-04'),
  dateFin: dayjs('2023-12-03'),
  dateValider: dayjs('2023-12-04T00:37'),
  dateOuvert: dayjs('2023-12-03'),
  dateCreation: dayjs('2023-12-04T09:23'),
};

export const sampleWithNewData: NewProduction = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
