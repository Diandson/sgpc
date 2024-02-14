import dayjs from 'dayjs/esm';
import { IPersonne } from 'app/entities/personne/personne.model';
import { ETATPRODUCTION } from 'app/entities/enumerations/etatproduction.model';

export interface IProduction {
  id: number;
  libelle?: string | null;
  fichier?: string | null;
  fichierContentType?: string | null;
  finish?: boolean | null;
  etat?: keyof typeof ETATPRODUCTION | null;
  validerPar?: string | null;
  dateDepot?: dayjs.Dayjs | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  dateValider?: dayjs.Dayjs | null;
  dateOuvert?: dayjs.Dayjs | null;
  dateCreation?: dayjs.Dayjs | null;
  personne?: Pick<IPersonne, 'id' | 'nom' | 'prenom'> | null;
  producteur?: Pick<IPersonne, 'id' | 'nom' | 'prenom'> | null;
  receveur?: Pick<IPersonne, 'id' | 'nom' | 'prenom'> | null;
}

export type NewProduction = Omit<IProduction, 'id'> & { id: null };
