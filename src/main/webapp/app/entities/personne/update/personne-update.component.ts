import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFiliale } from 'app/entities/filiale/filiale.model';
import { FilialeService } from 'app/entities/filiale/service/filiale.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { PersonneService } from '../service/personne.service';
import { IPersonne } from '../personne.model';
import { PersonneFormService, PersonneFormGroup } from './personne-form.service';

@Component({
  standalone: true,
  selector: 'jhi-personne-update',
  templateUrl: './personne-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PersonneUpdateComponent implements OnInit {
  isSaving = false;
  personne: IPersonne | null = null;

  filialesSharedCollection: IFiliale[] = [];
  usersSharedCollection: IUser[] = [];

  editForm: PersonneFormGroup = this.personneFormService.createPersonneFormGroup();

  constructor(
    protected personneService: PersonneService,
    protected personneFormService: PersonneFormService,
    protected filialeService: FilialeService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareFiliale = (o1: IFiliale | null, o2: IFiliale | null): boolean => this.filialeService.compareFiliale(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personne }) => {
      this.personne = personne;
      if (personne) {
        this.updateForm(personne);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const personne = this.personneFormService.getPersonne(this.editForm);
    if (personne.id !== null) {
      this.subscribeToSaveResponse(this.personneService.update(personne));
    } else {
      this.subscribeToSaveResponse(this.personneService.create(personne));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonne>>): void {
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

  protected updateForm(personne: IPersonne): void {
    this.personne = personne;
    this.personneFormService.resetForm(this.editForm, personne);

    this.filialesSharedCollection = this.filialeService.addFilialeToCollectionIfMissing<IFiliale>(
      this.filialesSharedCollection,
      personne.filiale,
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, personne.user);
  }

  protected loadRelationshipsOptions(): void {
    this.filialeService
      .query()
      .pipe(map((res: HttpResponse<IFiliale[]>) => res.body ?? []))
      .pipe(map((filiales: IFiliale[]) => this.filialeService.addFilialeToCollectionIfMissing<IFiliale>(filiales, this.personne?.filiale)))
      .subscribe((filiales: IFiliale[]) => (this.filialesSharedCollection = filiales));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.personne?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
