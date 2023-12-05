import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';
import { ETATPRODUCTION } from 'app/entities/enumerations/etatproduction.model';
import { ProductionService } from '../service/production.service';
import { IProduction } from '../production.model';
import { ProductionFormService, ProductionFormGroup } from './production-form.service';

@Component({
  standalone: true,
  selector: 'jhi-production-update',
  templateUrl: './production-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProductionUpdateComponent implements OnInit {
  isSaving = false;
  production: IProduction | null = null;
  eTATPRODUCTIONValues = Object.keys(ETATPRODUCTION);

  personnesSharedCollection: IPersonne[] = [];

  editForm: ProductionFormGroup = this.productionFormService.createProductionFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected productionService: ProductionService,
    protected productionFormService: ProductionFormService,
    protected personneService: PersonneService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  comparePersonne = (o1: IPersonne | null, o2: IPersonne | null): boolean => this.personneService.comparePersonne(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ production }) => {
      this.production = production;
      if (production) {
        this.updateForm(production);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('sgpcApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const production = this.productionFormService.getProduction(this.editForm);
    if (production.id !== null) {
      this.subscribeToSaveResponse(this.productionService.update(production));
    } else {
      this.subscribeToSaveResponse(this.productionService.create(production));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduction>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(production: IProduction): void {
    this.production = production;
    this.productionFormService.resetForm(this.editForm, production);

    this.personnesSharedCollection = this.personneService.addPersonneToCollectionIfMissing<IPersonne>(
      this.personnesSharedCollection,
      production.personne,
      production.producteur,
      production.receveur,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personneService
      .query()
      .pipe(map((res: HttpResponse<IPersonne[]>) => res.body ?? []))
      .pipe(
        map((personnes: IPersonne[]) =>
          this.personneService.addPersonneToCollectionIfMissing<IPersonne>(
            personnes,
            this.production?.personne,
            this.production?.producteur,
            this.production?.receveur,
          ),
        ),
      )
      .subscribe((personnes: IPersonne[]) => (this.personnesSharedCollection = personnes));
  }
}
