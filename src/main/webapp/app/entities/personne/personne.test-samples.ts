import { IPersonne, NewPersonne } from './personne.model';

export const sampleWithRequiredData: IPersonne = {
  id: 13460,
};

export const sampleWithPartialData: IPersonne = {
  id: 31387,
  nom: 'chut athlète avant',
  prenom: 'énergique insipide',
  titre: 'secours cependant compromettre',
  numeroDocument: 'membre titulaire à condition que',
  telephone: '0469511421',
};

export const sampleWithFullData: IPersonne = {
  id: 16198,
  nom: 'patientèle',
  prenom: 'sale',
  titre: 'en dépit de gratis sous couleur de',
  numeroDocument: 'en guise de prout areu areu',
  telephone: '0677810893',
};

export const sampleWithNewData: NewPersonne = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
