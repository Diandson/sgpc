import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFiliale } from '../filiale.model';
import { FilialeService } from '../service/filiale.service';

export const filialeResolve = (route: ActivatedRouteSnapshot): Observable<null | IFiliale> => {
  const id = route.params['id'];
  if (id) {
    return inject(FilialeService)
      .find(id)
      .pipe(
        mergeMap((filiale: HttpResponse<IFiliale>) => {
          if (filiale.body) {
            return of(filiale.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default filialeResolve;
