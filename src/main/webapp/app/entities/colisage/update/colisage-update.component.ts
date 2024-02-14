import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';
import { IColisage } from '../colisage.model';
import { ColisageService } from '../service/colisage.service';
import { ColisageFormService, ColisageFormGroup } from './colisage-form.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  standalone: true,
  selector: 'jhi-colisage-update',
  templateUrl: './colisage-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ColisageUpdateComponent implements OnInit {
  isSaving = false;
  colisage: IColisage | null = null;

  personnesSharedCollection: IPersonne[] = [];

  editForm: ColisageFormGroup = this.colisageFormService.createColisageFormGroup();

  constructor(
    protected colisageService: ColisageService,
    protected colisageFormService: ColisageFormService,
    protected personneService: PersonneService,
    protected activatedRoute: ActivatedRoute,
    protected activedModal: NgbActiveModal,
  ) {}

  comparePersonne = (o1: IPersonne | null, o2: IPersonne | null): boolean => this.personneService.comparePersonne(o1, o2);

  ngOnInit(): void {
    // this.activatedRoute.data.subscribe(({ colisage }) => {
    //   this.colisage = colisage;
    //   this.loadRelationshipsOptions();
    // });
    if (this.colisage) {
      this.updateForm(this.colisage);
    }
  }

  previousState(): void {
    // window.history.back();
    this.activedModal.dismiss();
  }

  save(): void {
    this.isSaving = true;
    const colisage = this.colisageFormService.getColisage(this.editForm);
    if (colisage.id !== null) {
      this.subscribeToSaveResponse(this.colisageService.update(colisage));
    } else {
      this.subscribeToSaveResponse(this.colisageService.create(colisage));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IColisage>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    // this.previousState();
    this.activedModal.close('success');
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(colisage: IColisage): void {
    this.colisage = colisage;
    this.colisageFormService.resetForm(this.editForm, colisage);

    this.personnesSharedCollection = this.personneService.addPersonneToCollectionIfMissing<IPersonne>(
      this.personnesSharedCollection,
      colisage.personne,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personneService
      .query()
      .pipe(map((res: HttpResponse<IPersonne[]>) => res.body ?? []))
      .pipe(
        map((personnes: IPersonne[]) =>
          this.personneService.addPersonneToCollectionIfMissing<IPersonne>(personnes, this.colisage?.personne),
        ),
      )
      .subscribe((personnes: IPersonne[]) => (this.personnesSharedCollection = personnes));
  }
}
