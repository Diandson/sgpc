import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FilialeService } from '../service/filiale.service';
import { IFiliale } from '../filiale.model';
import { FilialeFormService } from './filiale-form.service';

import { FilialeUpdateComponent } from './filiale-update.component';

describe('Filiale Management Update Component', () => {
  let comp: FilialeUpdateComponent;
  let fixture: ComponentFixture<FilialeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let filialeFormService: FilialeFormService;
  let filialeService: FilialeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), FilialeUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FilialeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FilialeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    filialeFormService = TestBed.inject(FilialeFormService);
    filialeService = TestBed.inject(FilialeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const filiale: IFiliale = { id: 456 };

      activatedRoute.data = of({ filiale });
      comp.ngOnInit();

      expect(comp.filiale).toEqual(filiale);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFiliale>>();
      const filiale = { id: 123 };
      jest.spyOn(filialeFormService, 'getFiliale').mockReturnValue(filiale);
      jest.spyOn(filialeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ filiale });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: filiale }));
      saveSubject.complete();

      // THEN
      expect(filialeFormService.getFiliale).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(filialeService.update).toHaveBeenCalledWith(expect.objectContaining(filiale));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFiliale>>();
      const filiale = { id: 123 };
      jest.spyOn(filialeFormService, 'getFiliale').mockReturnValue({ id: null });
      jest.spyOn(filialeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ filiale: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: filiale }));
      saveSubject.complete();

      // THEN
      expect(filialeFormService.getFiliale).toHaveBeenCalled();
      expect(filialeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFiliale>>();
      const filiale = { id: 123 };
      jest.spyOn(filialeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ filiale });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(filialeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
