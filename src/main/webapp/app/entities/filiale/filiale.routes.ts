import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FilialeComponent } from './list/filiale.component';
import { FilialeDetailComponent } from './detail/filiale-detail.component';
import { FilialeUpdateComponent } from './update/filiale-update.component';
import FilialeResolve from './route/filiale-routing-resolve.service';

const filialeRoute: Routes = [
  {
    path: '',
    component: FilialeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FilialeDetailComponent,
    resolve: {
      filiale: FilialeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FilialeUpdateComponent,
    resolve: {
      filiale: FilialeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FilialeUpdateComponent,
    resolve: {
      filiale: FilialeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default filialeRoute;
