import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProduction } from '../production.model';
import { ProductionService } from '../service/production.service';

export const productionResolve = (route: ActivatedRouteSnapshot): Observable<null | IProduction> => {
  const id = route.params['id'];
  if (id) {
    return inject(ProductionService)
      .find(id)
      .pipe(
        mergeMap((production: HttpResponse<IProduction>) => {
          if (production.body) {
            return of(production.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default productionResolve;
