import dayjs from 'dayjs/esm';

import { IProduction, NewProduction } from './production.model';

export const sampleWithRequiredData: IProduction = {
  id: 12256,
};

export const sampleWithPartialData: IProduction = {
  id: 18081,
  libelle: 'au-dessous de citer en guise de',
  finish: true,
  validerPar: 'mélancolique aussi',
  dateDepot: dayjs('2023-12-03'),
  dateFin: dayjs('2023-12-04'),
  dateValider: dayjs('2023-12-03T19:43'),
  finished: false,
};

export const sampleWithFullData: IProduction = {
  id: 7004,
  libelle: "à l'entour de dessous",
  fichier: '../fake-data/blob/hipster.png',
  fichierContentType: 'unknown',
  finish: true,
  etat: 'VALIDATION',
  validerPar: 'étant donné que',
  dateDepot: dayjs('2023-12-03'),
  dateDebut: dayjs('2023-12-04'),
  dateFin: dayjs('2023-12-04'),
  dateValider: dayjs('2023-12-04T02:45'),
  dateOuvert: dayjs('2023-12-04'),
  dateCreation: dayjs('2023-12-04T03:13'),
  fichierControle: '../fake-data/blob/hipster.png',
  fichierControleContentType: 'unknown',
  fichierReception: '../fake-data/blob/hipster.png',
  fichierReceptionContentType: 'unknown',
  finished: true,
};

export const sampleWithNewData: NewProduction = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
