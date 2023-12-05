import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IColisage } from '../colisage.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../colisage.test-samples';

import { ColisageService, RestColisage } from './colisage.service';

const requireRestSample: RestColisage = {
  ...sampleWithRequiredData,
  dateCreation: sampleWithRequiredData.dateCreation?.toJSON(),
};

describe('Colisage Service', () => {
  let service: ColisageService;
  let httpMock: HttpTestingController;
  let expectedResult: IColisage | IColisage[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ColisageService);
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

    it('should create a Colisage', () => {
      const colisage = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(colisage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Colisage', () => {
      const colisage = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(colisage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Colisage', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Colisage', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Colisage', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addColisageToCollectionIfMissing', () => {
      it('should add a Colisage to an empty array', () => {
        const colisage: IColisage = sampleWithRequiredData;
        expectedResult = service.addColisageToCollectionIfMissing([], colisage);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(colisage);
      });

      it('should not add a Colisage to an array that contains it', () => {
        const colisage: IColisage = sampleWithRequiredData;
        const colisageCollection: IColisage[] = [
          {
            ...colisage,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addColisageToCollectionIfMissing(colisageCollection, colisage);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Colisage to an array that doesn't contain it", () => {
        const colisage: IColisage = sampleWithRequiredData;
        const colisageCollection: IColisage[] = [sampleWithPartialData];
        expectedResult = service.addColisageToCollectionIfMissing(colisageCollection, colisage);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(colisage);
      });

      it('should add only unique Colisage to an array', () => {
        const colisageArray: IColisage[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const colisageCollection: IColisage[] = [sampleWithRequiredData];
        expectedResult = service.addColisageToCollectionIfMissing(colisageCollection, ...colisageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const colisage: IColisage = sampleWithRequiredData;
        const colisage2: IColisage = sampleWithPartialData;
        expectedResult = service.addColisageToCollectionIfMissing([], colisage, colisage2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(colisage);
        expect(expectedResult).toContain(colisage2);
      });

      it('should accept null and undefined values', () => {
        const colisage: IColisage = sampleWithRequiredData;
        expectedResult = service.addColisageToCollectionIfMissing([], null, colisage, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(colisage);
      });

      it('should return initial array if no Colisage is added', () => {
        const colisageCollection: IColisage[] = [sampleWithRequiredData];
        expectedResult = service.addColisageToCollectionIfMissing(colisageCollection, undefined, null);
        expect(expectedResult).toEqual(colisageCollection);
      });
    });

    describe('compareColisage', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareColisage(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareColisage(entity1, entity2);
        const compareResult2 = service.compareColisage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareColisage(entity1, entity2);
        const compareResult2 = service.compareColisage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareColisage(entity1, entity2);
        const compareResult2 = service.compareColisage(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
