import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ProductionComponent } from './list/production.component';
import { ProductionDetailComponent } from './detail/production-detail.component';
import { ProductionUpdateComponent } from './update/production-update.component';
import ProductionResolve from './route/production-routing-resolve.service';

const productionRoute: Routes = [
  {
    path: '',
    component: ProductionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductionDetailComponent,
    resolve: {
      production: ProductionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductionUpdateComponent,
    resolve: {
      production: ProductionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductionUpdateComponent,
    resolve: {
      production: ProductionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default productionRoute;
