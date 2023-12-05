import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProduction } from '../production.model';
import { ProductionService } from '../service/production.service';

@Component({
  standalone: true,
  templateUrl: './production-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProductionDeleteDialogComponent {
  production?: IProduction;

  constructor(
    protected productionService: ProductionService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
