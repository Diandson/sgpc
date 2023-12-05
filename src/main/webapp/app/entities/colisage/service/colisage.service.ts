import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IColisage, NewColisage } from '../colisage.model';

export type PartialUpdateColisage = Partial<IColisage> & Pick<IColisage, 'id'>;

type RestOf<T extends IColisage | NewColisage> = Omit<T, 'dateCreation'> & {
  dateCreation?: string | null;
};

export type RestColisage = RestOf<IColisage>;

export type NewRestColisage = RestOf<NewColisage>;

export type PartialUpdateRestColisage = RestOf<PartialUpdateColisage>;

export type EntityResponseType = HttpResponse<IColisage>;
export type EntityArrayResponseType = HttpResponse<IColisage[]>;

@Injectable({ providedIn: 'root' })
export class ColisageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/colisages');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(colisage: NewColisage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(colisage);
    return this.http
      .post<RestColisage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(colisage: IColisage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(colisage);
    return this.http
      .put<RestColisage>(`${this.resourceUrl}/${this.getColisageIdentifier(colisage)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(colisage: PartialUpdateColisage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(colisage);
    return this.http
      .patch<RestColisage>(`${this.resourceUrl}/${this.getColisageIdentifier(colisage)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestColisage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestColisage[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getColisageIdentifier(colisage: Pick<IColisage, 'id'>): number {
    return colisage.id;
  }

  compareColisage(o1: Pick<IColisage, 'id'> | null, o2: Pick<IColisage, 'id'> | null): boolean {
    return o1 && o2 ? this.getColisageIdentifier(o1) === this.getColisageIdentifier(o2) : o1 === o2;
  }

  addColisageToCollectionIfMissing<Type extends Pick<IColisage, 'id'>>(
    colisageCollection: Type[],
    ...colisagesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const colisages: Type[] = colisagesToCheck.filter(isPresent);
    if (colisages.length > 0) {
      const colisageCollectionIdentifiers = colisageCollection.map(colisageItem => this.getColisageIdentifier(colisageItem)!);
      const colisagesToAdd = colisages.filter(colisageItem => {
        const colisageIdentifier = this.getColisageIdentifier(colisageItem);
        if (colisageCollectionIdentifiers.includes(colisageIdentifier)) {
          return false;
        }
        colisageCollectionIdentifiers.push(colisageIdentifier);
        return true;
      });
      return [...colisagesToAdd, ...colisageCollection];
    }
    return colisageCollection;
  }

  protected convertDateFromClient<T extends IColisage | NewColisage | PartialUpdateColisage>(colisage: T): RestOf<T> {
    return {
      ...colisage,
      dateCreation: colisage.dateCreation?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restColisage: RestColisage): IColisage {
    return {
      ...restColisage,
      dateCreation: restColisage.dateCreation ? dayjs(restColisage.dateCreation) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestColisage>): HttpResponse<IColisage> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestColisage[]>): HttpResponse<IColisage[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
