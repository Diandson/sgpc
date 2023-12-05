import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ColisageComponent } from './list/colisage.component';
import { ColisageDetailComponent } from './detail/colisage-detail.component';
import { ColisageUpdateComponent } from './update/colisage-update.component';
import ColisageResolve from './route/colisage-routing-resolve.service';

const colisageRoute: Routes = [
  {
    path: '',
    component: ColisageComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ColisageDetailComponent,
    resolve: {
      colisage: ColisageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ColisageUpdateComponent,
    resolve: {
      colisage: ColisageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ColisageUpdateComponent,
    resolve: {
      colisage: ColisageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default colisageRoute;
