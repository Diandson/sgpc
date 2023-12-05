import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IProduction } from '../production.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../production.test-samples';

import { ProductionService, RestProduction } from './production.service';

const requireRestSample: RestProduction = {
  ...sampleWithRequiredData,
  dateDepot: sampleWithRequiredData.dateDepot?.format(DATE_FORMAT),
  dateDebut: sampleWithRequiredData.dateDebut?.format(DATE_FORMAT),
  dateFin: sampleWithRequiredData.dateFin?.format(DATE_FORMAT),
  dateValider: sampleWithRequiredData.dateValider?.toJSON(),
  dateOuvert: sampleWithRequiredData.dateOuvert?.format(DATE_FORMAT),
  dateCreation: sampleWithRequiredData.dateCreation?.toJSON(),
};

describe('Production Service', () => {
  let service: ProductionService;
  let httpMock: HttpTestingController;
  let expectedResult: IProduction | IProduction[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProductionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Production', () => {
      const production = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(production).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Production', () => {
      const production = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(production).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Production', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Production', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Production', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProductionToCollectionIfMissing', () => {
      it('should add a Production to an empty array', () => {
        const production: IProduction = sampleWithRequiredData;
        expectedResult = service.addProductionToCollectionIfMissing([], production);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(production);
      });

      it('should not add a Production to an array that contains it', () => {
        const production: IProduction = sampleWithRequiredData;
        const productionCollection: IProduction[] = [
          {
            ...production,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductionToCollectionIfMissing(productionCollection, production);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Production to an array that doesn't contain it", () => {
        const production: IProduction = sampleWithRequiredData;
        const productionCollection: IProduction[] = [sampleWithPartialData];
        expectedResult = service.addProductionToCollectionIfMissing(productionCollection, production);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(production);
      });

      it('should add only unique Production to an array', () => {
        const productionArray: IProduction[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productionCollection: IProduction[] = [sampleWithRequiredData];
        expectedResult = service.addProductionToCollectionIfMissing(productionCollection, ...productionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const production: IProduction = sampleWithRequiredData;
        const production2: IProduction = sampleWithPartialData;
        expectedResult = service.addProductionToCollectionIfMissing([], production, production2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(production);
        expect(expectedResult).toContain(production2);
      });

      it('should accept null and undefined values', () => {
        const production: IProduction = sampleWithRequiredData;
        expectedResult = service.addProductionToCollectionIfMissing([], null, production, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(production);
      });

      it('should return initial array if no Production is added', () => {
        const productionCollection: IProduction[] = [sampleWithRequiredData];
        expectedResult = service.addProductionToCollectionIfMissing(productionCollection, undefined, null);
        expect(expectedResult).toEqual(productionCollection);
      });
    });

    describe('compareProduction', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProduction(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProduction(entity1, entity2);
        const compareResult2 = service.compareProduction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProduction(entity1, entity2);
        const compareResult2 = service.compareProduction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProduction(entity1, entity2);
        const compareResult2 = service.compareProduction(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
