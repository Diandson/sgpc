import dayjs from 'dayjs/esm';
import { IPersonne } from 'app/entities/personne/personne.model';
import { IProduction } from '../production/production.model';

export interface IColisage {
  id: number;
  destination?: string | null;
  canal?: string | null;
  recuPar?: string | null;
  estRecu?: boolean | null;
  dateCreation?: dayjs.Dayjs | null;
  personne?: Pick<IPersonne, 'id'> | null;
  productionList?: IProduction[] | null;
}

export type NewColisage = Omit<IColisage, 'id'> & { id: null };
