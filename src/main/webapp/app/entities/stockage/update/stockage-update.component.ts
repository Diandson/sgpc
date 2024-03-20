import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';
import { IStockage } from '../stockage.model';
import { StockageService } from '../service/stockage.service';
import { StockageFormService, StockageFormGroup } from './stockage-form.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  standalone: true,
  selector: 'jhi-stockage-update',
  templateUrl: './stockage-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StockageUpdateComponent implements OnInit {
  isSaving = false;
  stockage: IStockage | null = null;

  personnesSharedCollection: IPersonne[] = [];

  editForm: StockageFormGroup = this.stockageFormService.createStockageFormGroup();

  constructor(
    protected stockageService: StockageService,
    protected stockageFormService: StockageFormService,
    protected personneService: PersonneService,
    protected activatedRoute: ActivatedRoute,
    protected activeModal: NgbActiveModal,
  ) {}

  comparePersonne = (o1: IPersonne | null, o2: IPersonne | null): boolean => this.personneService.comparePersonne(o1, o2);

  ngOnInit(): void {
    if (this.stockage) {
      this.updateForm(this.stockage);
    }
  }

  previousState(): void {
    // window.history.back();
    this.activeModal.dismiss();
  }

  save(): void {
    this.isSaving = true;
    const stockage = this.stockageFormService.getStockage(this.editForm);
    if (stockage.id !== null) {
      this.subscribeToSaveResponse(this.stockageService.update(stockage));
    } else {
      this.subscribeToSaveResponse(this.stockageService.create(stockage));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStockage>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    // this.previousState();
    this.activeModal.close('success');
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(stockage: IStockage): void {
    this.stockage = stockage;
    this.stockageFormService.resetForm(this.editForm, stockage);

    this.personnesSharedCollection = this.personneService.addPersonneToCollectionIfMissing<IPersonne>(
      this.personnesSharedCollection,
      stockage.personne,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personneService
      .query()
      .pipe(map((res: HttpResponse<IPersonne[]>) => res.body ?? []))
      .pipe(
        map((personnes: IPersonne[]) =>
          this.personneService.addPersonneToCollectionIfMissing<IPersonne>(personnes, this.stockage?.personne),
        ),
      )
      .subscribe((personnes: IPersonne[]) => (this.personnesSharedCollection = personnes));
  }
}
