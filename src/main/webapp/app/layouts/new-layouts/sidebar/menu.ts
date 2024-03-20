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
    icon: 'fa-cog',
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
    icon: 'fa-user',
    subItems: [
      {
        id: 5,
        label: 'Utilisateurs',
        icon: 'fa-user',
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
    icon: 'fa-calendar',
    link: '/production',
  },
  {
    id: 8,
    label: 'Colisage',
    icon: 'fa-envelope',
    link: '/colisage',
  },
  {
    id: 8,
    label: 'Mails',
    icon: 'fa-envelope',
    link: '/email',
  },
  {
    id: 9,
    label: 'Stockage',
    icon: 'fa-server',
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
    icon: 'fa-calendar',
    link: '/production',
  },
  {
    id: 2,
    label: 'Colisage',
    icon: 'fa-envelope',
    link: '/colisage',
  },
];
export const MENU_PROD: MenuItem[] = [
  {
    id: 1,
    label: 'OPERATIONS',
    isTitle: true,
  },
  {
    id: 2,
    label: 'Productions',
    icon: 'fa-calendar',
    link: '/production',
  },
  {
    id: 2,
    label: 'Colisage',
    icon: 'fa-envelope',
    link: '/colisage',
  },
  {
    id: 3,
    label: 'Stockage',
    icon: 'fa-server',
    link: '/stockage',
  },
];
