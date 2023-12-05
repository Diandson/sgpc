import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFiliale } from '../filiale.model';
import { FilialeService } from '../service/filiale.service';

@Component({
  standalone: true,
  templateUrl: './filiale-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FilialeDeleteDialogComponent {
  filiale?: IFiliale;

  constructor(
    protected filialeService: FilialeService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.filialeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
