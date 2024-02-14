import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { LANGUAGES } from 'app/config/language.constants';
import { IUser } from '../user-management.model';
import { UserManagementService } from '../service/user-management.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IFiliale } from '../../../entities/filiale/filiale.model';
import { map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { FilialeService } from '../../../entities/filiale/service/filiale.service';
import { IPersonne } from '../../../entities/personne/personne.model';

const userTemplate = {} as IUser;
const personneTemplate = {} as IPersonne;

const newUser: IUser = {
  langKey: 'fr',
  activated: true,
} as IUser;

@Component({
  standalone: true,
  selector: 'jhi-user-mgmt-update',
  templateUrl: './user-management-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export default class UserManagementUpdateComponent implements OnInit {
  languages = LANGUAGES;
  authorities: string[] = [];
  isSaving = false;
  user?: IUser;
  filialesSharedCollection: IFiliale[] = [];

  editForm = new FormGroup({
    id: new FormControl(userTemplate.id),
    login: new FormControl(userTemplate.login, {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(50),
        Validators.pattern('^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$'),
      ],
    }),
    firstName: new FormControl(userTemplate.firstName, { validators: [Validators.maxLength(50)] }),
    lastName: new FormControl(userTemplate.lastName, { validators: [Validators.maxLength(50)] }),
    email: new FormControl(userTemplate.email, {
      nonNullable: true,
      validators: [Validators.minLength(5), Validators.maxLength(254), Validators.email],
    }),
    activated: new FormControl(userTemplate.activated, { nonNullable: true }),
    langKey: new FormControl(userTemplate.langKey, { nonNullable: true }),
    authorities: new FormControl(userTemplate.authorities, { nonNullable: true }),
    telephone: new FormControl(personneTemplate.telephone, { nonNullable: true }),
    titre: new FormControl(personneTemplate.titre!, { nonNullable: true }),
    numeroDocument: new FormControl(personneTemplate.numeroDocument!, { nonNullable: true }),
    filiale: new FormControl(personneTemplate.filiale!, { nonNullable: true }),
    personne: new FormControl(userTemplate.personne, { nonNullable: true }),
  });

  constructor(
    private userService: UserManagementService,
    private route: ActivatedRoute,
    protected filialeService: FilialeService,
    private activedModal: NgbActiveModal,
  ) {}

  ngOnInit(): void {
    // this.route.data.subscribe(({ user }) => {
    //   if (user) {
    //     this.editForm.reset(user);
    //   } else {
    //     this.editForm.reset(newUser);
    //   }
    // });
    if (this.user) {
      this.editForm.reset(this.user);
    }
    this.userService.authorities().subscribe(authorities => (this.authorities = authorities));

    this.filialeService
      .query()
      .pipe(map((res: HttpResponse<IFiliale[]>) => res.body ?? []))
      .pipe(
        map((filiales: IFiliale[]) =>
          this.filialeService.addFilialeToCollectionIfMissing<IFiliale>(filiales, this.user?.personne?.filiale),
        ),
      )
      .subscribe((filiales: IFiliale[]) => (this.filialesSharedCollection = filiales));
  }

  previousState(): void {
    // window.history.back();
    this.activedModal.dismiss();
  }

  save(): void {
    this.isSaving = true;
    const user = this.editForm.getRawValue();
    const personne = {} as IPersonne;
    personne.filiale = user.filiale;
    personne.telephone = user.telephone;
    personne.titre = user.titre;
    personne.numeroDocument = user.numeroDocument;
    user.personne = personne;
    if (user.id !== null) {
      this.userService.update(user).subscribe({
        next: () => this.onSaveSuccess(),
        error: () => this.onSaveError(),
      });
    } else {
      this.userService.create(user).subscribe({
        next: () => this.onSaveSuccess(),
        error: () => this.onSaveError(),
      });
    }
  }

  private onSaveSuccess(): void {
    this.isSaving = false;
    // this.previousState();
    this.activedModal.close('success');
  }

  private onSaveError(): void {
    this.isSaving = false;
  }
}
