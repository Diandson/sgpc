import { IFiliale } from 'app/entities/filiale/filiale.model';
import { IUser } from 'app/entities/user/user.model';

export interface IPersonne {
  id: number;
  nom?: string | null;
  prenom?: string | null;
  titre?: string | null;
  numeroDocument?: string | null;
  telephone?: string | null;
  filiale?: Pick<IFiliale, 'id' | 'denomination'> | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewPersonne = Omit<IPersonne, 'id'> & { id: null };
