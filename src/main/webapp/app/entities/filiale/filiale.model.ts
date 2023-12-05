export interface IFiliale {
  id: number;
  denomination?: string | null;
  sigle?: string | null;
}

export type NewFiliale = Omit<IFiliale, 'id'> & { id: null };
