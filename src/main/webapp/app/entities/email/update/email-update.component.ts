import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IColisage } from 'app/entities/colisage/colisage.model';
import { ColisageService } from 'app/entities/colisage/service/colisage.service';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';
import { EmailService } from '../service/email.service';
import { IEmail } from '../email.model';
import { EmailFormService, EmailFormGroup } from './email-form.service';

@Component({
  standalone: true,
  selector: 'jhi-email-update',
  templateUrl: './email-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EmailUpdateComponent implements OnInit {
  isSaving = false;
  email: IEmail | null = null;

  colisagesSharedCollection: IColisage[] = [];
  personnesSharedCollection: IPersonne[] = [];

  editForm: EmailFormGroup = this.emailFormService.createEmailFormGroup();

  constructor(
    protected emailService: EmailService,
    protected emailFormService: EmailFormService,
    protected colisageService: ColisageService,
    protected personneService: PersonneService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareColisage = (o1: IColisage | null, o2: IColisage | null): boolean => this.colisageService.compareColisage(o1, o2);

  comparePersonne = (o1: IPersonne | null, o2: IPersonne | null): boolean => this.personneService.comparePersonne(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ email }) => {
      this.email = email;
      if (email) {
        this.updateForm(email);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const email = this.emailFormService.getEmail(this.editForm);
    if (email.id !== null) {
      this.subscribeToSaveResponse(this.emailService.update(email));
    } else {
      this.subscribeToSaveResponse(this.emailService.create(email));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmail>>): void {
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

  protected updateForm(email: IEmail): void {
    this.email = email;
    this.emailFormService.resetForm(this.editForm, email);

    this.colisagesSharedCollection = this.colisageService.addColisageToCollectionIfMissing<IColisage>(
      this.colisagesSharedCollection,
      email.colisage,
    );
    this.personnesSharedCollection = this.personneService.addPersonneToCollectionIfMissing<IPersonne>(
      this.personnesSharedCollection,
      email.personne,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.colisageService
      .query()
      .pipe(map((res: HttpResponse<IColisage[]>) => res.body ?? []))
      .pipe(
        map((colisages: IColisage[]) => this.colisageService.addColisageToCollectionIfMissing<IColisage>(colisages, this.email?.colisage)),
      )
      .subscribe((colisages: IColisage[]) => (this.colisagesSharedCollection = colisages));

    this.personneService
      .query()
      .pipe(map((res: HttpResponse<IPersonne[]>) => res.body ?? []))
      .pipe(
        map((personnes: IPersonne[]) => this.personneService.addPersonneToCollectionIfMissing<IPersonne>(personnes, this.email?.personne)),
      )
      .subscribe((personnes: IPersonne[]) => (this.personnesSharedCollection = personnes));
  }
}
