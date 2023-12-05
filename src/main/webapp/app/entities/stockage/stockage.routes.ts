import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { StockageComponent } from './list/stockage.component';
import { StockageDetailComponent } from './detail/stockage-detail.component';
import { StockageUpdateComponent } from './update/stockage-update.component';
import StockageResolve from './route/stockage-routing-resolve.service';

const stockageRoute: Routes = [
  {
    path: '',
    component: StockageComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StockageDetailComponent,
    resolve: {
      stockage: StockageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StockageUpdateComponent,
    resolve: {
      stockage: StockageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StockageUpdateComponent,
    resolve: {
      stockage: StockageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default stockageRoute;
