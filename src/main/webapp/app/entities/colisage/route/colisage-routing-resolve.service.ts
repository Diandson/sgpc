import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IColisage } from '../colisage.model';
import { ColisageService } from '../service/colisage.service';

export const colisageResolve = (route: ActivatedRouteSnapshot): Observable<null | IColisage> => {
  const id = route.params['id'];
  if (id) {
    return inject(ColisageService)
      .find(id)
      .pipe(
        mergeMap((colisage: HttpResponse<IColisage>) => {
          if (colisage.body) {
            return of(colisage.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default colisageResolve;
