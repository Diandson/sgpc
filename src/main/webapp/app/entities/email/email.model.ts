import dayjs from 'dayjs/esm';
import { IColisage } from 'app/entities/colisage/colisage.model';
import { IPersonne } from 'app/entities/personne/personne.model';

export interface IEmail {
  id: number;
  objet?: string | null;
  contenu?: string | null;
  destinataire?: string | null;
  dateEnvoi?: dayjs.Dayjs | null;
  colisage?: Pick<IColisage, 'id'> | null;
  personne?: Pick<IPersonne, 'id'> | null;
}

export type NewEmail = Omit<IEmail, 'id'> & { id: null };
