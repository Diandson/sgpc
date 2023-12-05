import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'filiale',
        data: { pageTitle: 'sgpcApp.filiale.home.title' },
        loadChildren: () => import('./filiale/filiale.routes'),
      },
      {
        path: 'personne',
        data: { pageTitle: 'sgpcApp.personne.home.title' },
        loadChildren: () => import('./personne/personne.routes'),
      },
      {
        path: 'production',
        data: { pageTitle: 'sgpcApp.production.home.title' },
        loadChildren: () => import('./production/production.routes'),
      },
      {
        path: 'colisage',
        data: { pageTitle: 'sgpcApp.colisage.home.title' },
        loadChildren: () => import('./colisage/colisage.routes'),
      },
      {
        path: 'stockage',
        data: { pageTitle: 'sgpcApp.stockage.home.title' },
        loadChildren: () => import('./stockage/stockage.routes'),
      },
      {
        path: 'email',
        data: { pageTitle: 'sgpcApp.email.home.title' },
        loadChildren: () => import('./email/email.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
