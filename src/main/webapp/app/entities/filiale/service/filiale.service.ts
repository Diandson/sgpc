import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFiliale, NewFiliale } from '../filiale.model';

export type PartialUpdateFiliale = Partial<IFiliale> & Pick<IFiliale, 'id'>;

export type EntityResponseType = HttpResponse<IFiliale>;
export type EntityArrayResponseType = HttpResponse<IFiliale[]>;

@Injectable({ providedIn: 'root' })
export class FilialeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/filiales');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(filiale: NewFiliale): Observable<EntityResponseType> {
    return this.http.post<IFiliale>(this.resourceUrl, filiale, { observe: 'response' });
  }

  update(filiale: IFiliale): Observable<EntityResponseType> {
    return this.http.put<IFiliale>(`${this.resourceUrl}/${this.getFilialeIdentifier(filiale)}`, filiale, { observe: 'response' });
  }

  partialUpdate(filiale: PartialUpdateFiliale): Observable<EntityResponseType> {
    return this.http.patch<IFiliale>(`${this.resourceUrl}/${this.getFilialeIdentifier(filiale)}`, filiale, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFiliale>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFiliale[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFilialeIdentifier(filiale: Pick<IFiliale, 'id'>): number {
    return filiale.id;
  }

  compareFiliale(o1: Pick<IFiliale, 'id'> | null, o2: Pick<IFiliale, 'id'> | null): boolean {
    return o1 && o2 ? this.getFilialeIdentifier(o1) === this.getFilialeIdentifier(o2) : o1 === o2;
  }

  addFilialeToCollectionIfMissing<Type extends Pick<IFiliale, 'id'>>(
    filialeCollection: Type[],
    ...filialesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const filiales: Type[] = filialesToCheck.filter(isPresent);
    if (filiales.length > 0) {
      const filialeCollectionIdentifiers = filialeCollection.map(filialeItem => this.getFilialeIdentifier(filialeItem)!);
      const filialesToAdd = filiales.filter(filialeItem => {
        const filialeIdentifier = this.getFilialeIdentifier(filialeItem);
        if (filialeCollectionIdentifiers.includes(filialeIdentifier)) {
          return false;
        }
        filialeCollectionIdentifiers.push(filialeIdentifier);
        return true;
      });
      return [...filialesToAdd, ...filialeCollection];
    }
    return filialeCollection;
  }
}
