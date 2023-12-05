import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStockage } from '../stockage.model';
import { StockageService } from '../service/stockage.service';

export const stockageResolve = (route: ActivatedRouteSnapshot): Observable<null | IStockage> => {
  const id = route.params['id'];
  if (id) {
    return inject(StockageService)
      .find(id)
      .pipe(
        mergeMap((stockage: HttpResponse<IStockage>) => {
          if (stockage.body) {
            return of(stockage.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default stockageResolve;
