import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IStockage } from '../stockage.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../stockage.test-samples';

import { StockageService, RestStockage } from './stockage.service';

const requireRestSample: RestStockage = {
  ...sampleWithRequiredData,
  dateCreation: sampleWithRequiredData.dateCreation?.toJSON(),
};

describe('Stockage Service', () => {
  let service: StockageService;
  let httpMock: HttpTestingController;
  let expectedResult: IStockage | IStockage[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(StockageService);
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

    it('should create a Stockage', () => {
      const stockage = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(stockage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Stockage', () => {
      const stockage = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(stockage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Stockage', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Stockage', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Stockage', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addStockageToCollectionIfMissing', () => {
      it('should add a Stockage to an empty array', () => {
        const stockage: IStockage = sampleWithRequiredData;
        expectedResult = service.addStockageToCollectionIfMissing([], stockage);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stockage);
      });

      it('should not add a Stockage to an array that contains it', () => {
        const stockage: IStockage = sampleWithRequiredData;
        const stockageCollection: IStockage[] = [
          {
            ...stockage,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStockageToCollectionIfMissing(stockageCollection, stockage);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Stockage to an array that doesn't contain it", () => {
        const stockage: IStockage = sampleWithRequiredData;
        const stockageCollection: IStockage[] = [sampleWithPartialData];
        expectedResult = service.addStockageToCollectionIfMissing(stockageCollection, stockage);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stockage);
      });

      it('should add only unique Stockage to an array', () => {
        const stockageArray: IStockage[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const stockageCollection: IStockage[] = [sampleWithRequiredData];
        expectedResult = service.addStockageToCollectionIfMissing(stockageCollection, ...stockageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const stockage: IStockage = sampleWithRequiredData;
        const stockage2: IStockage = sampleWithPartialData;
        expectedResult = service.addStockageToCollectionIfMissing([], stockage, stockage2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stockage);
        expect(expectedResult).toContain(stockage2);
      });

      it('should accept null and undefined values', () => {
        const stockage: IStockage = sampleWithRequiredData;
        expectedResult = service.addStockageToCollectionIfMissing([], null, stockage, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stockage);
      });

      it('should return initial array if no Stockage is added', () => {
        const stockageCollection: IStockage[] = [sampleWithRequiredData];
        expectedResult = service.addStockageToCollectionIfMissing(stockageCollection, undefined, null);
        expect(expectedResult).toEqual(stockageCollection);
      });
    });

    describe('compareStockage', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStockage(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareStockage(entity1, entity2);
        const compareResult2 = service.compareStockage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareStockage(entity1, entity2);
        const compareResult2 = service.compareStockage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareStockage(entity1, entity2);
        const compareResult2 = service.compareStockage(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
