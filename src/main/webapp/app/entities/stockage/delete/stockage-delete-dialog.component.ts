import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IStockage } from '../stockage.model';
import { StockageService } from '../service/stockage.service';

@Component({
  standalone: true,
  templateUrl: './stockage-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class StockageDeleteDialogComponent {
  stockage?: IStockage;

  constructor(
    protected stockageService: StockageService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.stockageService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
