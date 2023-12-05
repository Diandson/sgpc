import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFiliale } from '../filiale.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../filiale.test-samples';

import { FilialeService } from './filiale.service';

const requireRestSample: IFiliale = {
  ...sampleWithRequiredData,
};

describe('Filiale Service', () => {
  let service: FilialeService;
  let httpMock: HttpTestingController;
  let expectedResult: IFiliale | IFiliale[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FilialeService);
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

    it('should create a Filiale', () => {
      const filiale = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(filiale).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Filiale', () => {
      const filiale = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(filiale).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Filiale', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Filiale', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Filiale', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFilialeToCollectionIfMissing', () => {
      it('should add a Filiale to an empty array', () => {
        const filiale: IFiliale = sampleWithRequiredData;
        expectedResult = service.addFilialeToCollectionIfMissing([], filiale);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(filiale);
      });

      it('should not add a Filiale to an array that contains it', () => {
        const filiale: IFiliale = sampleWithRequiredData;
        const filialeCollection: IFiliale[] = [
          {
            ...filiale,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFilialeToCollectionIfMissing(filialeCollection, filiale);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Filiale to an array that doesn't contain it", () => {
        const filiale: IFiliale = sampleWithRequiredData;
        const filialeCollection: IFiliale[] = [sampleWithPartialData];
        expectedResult = service.addFilialeToCollectionIfMissing(filialeCollection, filiale);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(filiale);
      });

      it('should add only unique Filiale to an array', () => {
        const filialeArray: IFiliale[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const filialeCollection: IFiliale[] = [sampleWithRequiredData];
        expectedResult = service.addFilialeToCollectionIfMissing(filialeCollection, ...filialeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const filiale: IFiliale = sampleWithRequiredData;
        const filiale2: IFiliale = sampleWithPartialData;
        expectedResult = service.addFilialeToCollectionIfMissing([], filiale, filiale2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(filiale);
        expect(expectedResult).toContain(filiale2);
      });

      it('should accept null and undefined values', () => {
        const filiale: IFiliale = sampleWithRequiredData;
        expectedResult = service.addFilialeToCollectionIfMissing([], null, filiale, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(filiale);
      });

      it('should return initial array if no Filiale is added', () => {
        const filialeCollection: IFiliale[] = [sampleWithRequiredData];
        expectedResult = service.addFilialeToCollectionIfMissing(filialeCollection, undefined, null);
        expect(expectedResult).toEqual(filialeCollection);
      });
    });

    describe('compareFiliale', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFiliale(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFiliale(entity1, entity2);
        const compareResult2 = service.compareFiliale(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFiliale(entity1, entity2);
        const compareResult2 = service.compareFiliale(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFiliale(entity1, entity2);
        const compareResult2 = service.compareFiliale(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
