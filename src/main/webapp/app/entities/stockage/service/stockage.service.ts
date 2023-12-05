import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStockage, NewStockage } from '../stockage.model';

export type PartialUpdateStockage = Partial<IStockage> & Pick<IStockage, 'id'>;

type RestOf<T extends IStockage | NewStockage> = Omit<T, 'dateCreation'> & {
  dateCreation?: string | null;
};

export type RestStockage = RestOf<IStockage>;

export type NewRestStockage = RestOf<NewStockage>;

export type PartialUpdateRestStockage = RestOf<PartialUpdateStockage>;

export type EntityResponseType = HttpResponse<IStockage>;
export type EntityArrayResponseType = HttpResponse<IStockage[]>;

@Injectable({ providedIn: 'root' })
export class StockageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/stockages');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(stockage: NewStockage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(stockage);
    return this.http
      .post<RestStockage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(stockage: IStockage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(stockage);
    return this.http
      .put<RestStockage>(`${this.resourceUrl}/${this.getStockageIdentifier(stockage)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(stockage: PartialUpdateStockage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(stockage);
    return this.http
      .patch<RestStockage>(`${this.resourceUrl}/${this.getStockageIdentifier(stockage)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestStockage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestStockage[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStockageIdentifier(stockage: Pick<IStockage, 'id'>): number {
    return stockage.id;
  }

  compareStockage(o1: Pick<IStockage, 'id'> | null, o2: Pick<IStockage, 'id'> | null): boolean {
    return o1 && o2 ? this.getStockageIdentifier(o1) === this.getStockageIdentifier(o2) : o1 === o2;
  }

  addStockageToCollectionIfMissing<Type extends Pick<IStockage, 'id'>>(
    stockageCollection: Type[],
    ...stockagesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const stockages: Type[] = stockagesToCheck.filter(isPresent);
    if (stockages.length > 0) {
      const stockageCollectionIdentifiers = stockageCollection.map(stockageItem => this.getStockageIdentifier(stockageItem)!);
      const stockagesToAdd = stockages.filter(stockageItem => {
        const stockageIdentifier = this.getStockageIdentifier(stockageItem);
        if (stockageCollectionIdentifiers.includes(stockageIdentifier)) {
          return false;
        }
        stockageCollectionIdentifiers.push(stockageIdentifier);
        return true;
      });
      return [...stockagesToAdd, ...stockageCollection];
    }
    return stockageCollection;
  }

  protected convertDateFromClient<T extends IStockage | NewStockage | PartialUpdateStockage>(stockage: T): RestOf<T> {
    return {
      ...stockage,
      dateCreation: stockage.dateCreation?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restStockage: RestStockage): IStockage {
    return {
      ...restStockage,
      dateCreation: restStockage.dateCreation ? dayjs(restStockage.dateCreation) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestStockage>): HttpResponse<IStockage> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestStockage[]>): HttpResponse<IStockage[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
