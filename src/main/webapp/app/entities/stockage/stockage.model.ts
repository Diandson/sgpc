import dayjs from 'dayjs/esm';
import { IPersonne } from 'app/entities/personne/personne.model';

export interface IStockage {
  id: number;
  denomination?: string | null;
  code?: string | null;
  quantite?: string | null;
  modifierPar?: string | null;
  dateCreation?: dayjs.Dayjs | null;
  personne?: Pick<IPersonne, 'id'> | null;
}

export type NewStockage = Omit<IStockage, 'id'> & { id: null };
