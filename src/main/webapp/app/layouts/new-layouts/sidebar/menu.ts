import { MenuItem } from './menu.model';

export const MENU: MenuItem[] = [
  {
    id: 1,
    label: 'ADMINISTRATION',
    isTitle: true,
  },
  {
    id: 1,
    label: 'Parametres',
    icon: 'bx-user-circle',
    subItems: [
      {
        id: 2,
        label: 'Filiale',
        link: '/filiale',
        parentId: 1,
      },
      {
        id: 3,
        label: 'Personnel',
        link: '/personne',
        parentId: 1,
      },
    ],
  },
  {
    id: 4,
    label: 'COMPTES',
    icon: 'bx-user',
    subItems: [
      {
        id: 5,
        label: 'Utilisateurs',
        icon: 'bx-user',
        link: '/admin/user-management',
        parentId: 4,
      },
    ],
  },

  {
    id: 6,
    label: 'OPERATIONS',
    isTitle: true,
  },
  {
    id: 7,
    label: 'Productions',
    icon: 'bx-calendar',
    link: '/production',
  },
  {
    id: 8,
    label: 'Colis',
    icon: 'bx-layout',
    link: '/colisage',
  },
  {
    id: 9,
    label: 'Stockage',
    icon: 'bx-server',
    link: '/stockage',
  },
];
export const MENU_USER: MenuItem[] = [
  {
    id: 1,
    label: 'OPERATIONS',
    isTitle: true,
  },
  {
    id: 2,
    label: 'Productions',
    icon: 'bx-calendar',
    link: '/production',
  },
  {
    id: 2,
    label: 'Colis',
    icon: 'bx-calendar',
    link: '/colisage',
  },
];
