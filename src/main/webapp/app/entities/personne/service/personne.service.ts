import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersonne, NewPersonne } from '../personne.model';

export type PartialUpdatePersonne = Partial<IPersonne> & Pick<IPersonne, 'id'>;

export type EntityResponseType = HttpResponse<IPersonne>;
export type EntityArrayResponseType = HttpResponse<IPersonne[]>;

@Injectable({ providedIn: 'root' })
export class PersonneService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/personnes');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(personne: NewPersonne): Observable<EntityResponseType> {
    return this.http.post<IPersonne>(this.resourceUrl, personne, { observe: 'response' });
  }

  update(personne: IPersonne): Observable<EntityResponseType> {
    return this.http.put<IPersonne>(`${this.resourceUrl}/${this.getPersonneIdentifier(personne)}`, personne, { observe: 'response' });
  }

  partialUpdate(personne: PartialUpdatePersonne): Observable<EntityResponseType> {
    return this.http.patch<IPersonne>(`${this.resourceUrl}/${this.getPersonneIdentifier(personne)}`, personne, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPersonne>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPersonne[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPersonneIdentifier(personne: Pick<IPersonne, 'id'>): number {
    return personne.id;
  }

  comparePersonne(o1: Pick<IPersonne, 'id'> | null, o2: Pick<IPersonne, 'id'> | null): boolean {
    return o1 && o2 ? this.getPersonneIdentifier(o1) === this.getPersonneIdentifier(o2) : o1 === o2;
  }

  addPersonneToCollectionIfMissing<Type extends Pick<IPersonne, 'id'>>(
    personneCollection: Type[],
    ...personnesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const personnes: Type[] = personnesToCheck.filter(isPresent);
    if (personnes.length > 0) {
      const personneCollectionIdentifiers = personneCollection.map(personneItem => this.getPersonneIdentifier(personneItem)!);
      const personnesToAdd = personnes.filter(personneItem => {
        const personneIdentifier = this.getPersonneIdentifier(personneItem);
        if (personneCollectionIdentifiers.includes(personneIdentifier)) {
          return false;
        }
        personneCollectionIdentifiers.push(personneIdentifier);
        return true;
      });
      return [...personnesToAdd, ...personneCollection];
    }
    return personneCollection;
  }
}
