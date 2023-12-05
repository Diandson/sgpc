import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFiliale } from '../filiale.model';
import { FilialeService } from '../service/filiale.service';
import { FilialeFormService, FilialeFormGroup } from './filiale-form.service';

@Component({
  standalone: true,
  selector: 'jhi-filiale-update',
  templateUrl: './filiale-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FilialeUpdateComponent implements OnInit {
  isSaving = false;
  filiale: IFiliale | null = null;

  editForm: FilialeFormGroup = this.filialeFormService.createFilialeFormGroup();

  constructor(
    protected filialeService: FilialeService,
    protected filialeFormService: FilialeFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ filiale }) => {
      this.filiale = filiale;
      if (filiale) {
        this.updateForm(filiale);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const filiale = this.filialeFormService.getFiliale(this.editForm);
    if (filiale.id !== null) {
      this.subscribeToSaveResponse(this.filialeService.update(filiale));
    } else {
      this.subscribeToSaveResponse(this.filialeService.create(filiale));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFiliale>>): void {
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

  protected updateForm(filiale: IFiliale): void {
    this.filiale = filiale;
    this.filialeFormService.resetForm(this.editForm, filiale);
  }
}
