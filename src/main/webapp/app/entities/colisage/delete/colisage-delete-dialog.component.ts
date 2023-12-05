import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IColisage } from '../colisage.model';
import { ColisageService } from '../service/colisage.service';

@Component({
  standalone: true,
  templateUrl: './colisage-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ColisageDeleteDialogComponent {
  colisage?: IColisage;

  constructor(
    protected colisageService: ColisageService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.colisageService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
