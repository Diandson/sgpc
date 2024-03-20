import { Component, ElementRef, Input, ViewChild } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { DataUtils } from 'app/core/util/data-util.service';
import { IProduction } from '../production.model';
import { Authority } from '../../../config/authority.constants';
import { NgIf } from '@angular/common';
import HasAnyAuthorityDirective from '../../../shared/auth/has-any-authority.directive';
import { NzTagModule } from 'ng-zorro-antd/tag';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { ProductionService } from '../service/production.service';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ProductionUpdateComponent } from '../update/production-update.component';
import { ETATPRODUCTION } from '../../enumerations/etatproduction.model';
import { DateAgoPipe } from '../../../shared/date-ago.pipe';

@Component({
  standalone: true,
  selector: 'jhi-production-detail',
  templateUrl: './production-detail.component.html',
  imports: [
    SharedModule,
    RouterModule,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    NgIf,
    HasAnyAuthorityDirective,
    NzTagModule,
    NzIconModule,
    DateAgoPipe,
  ],
})
export class ProductionDetailComponent {
  @Input() production: IProduction | null = null;
  @ViewChild('content') modal?: ElementRef;
  auth = Authority;

  constructor(
    protected dataUtils: DataUtils,
    protected activatedRoute: ActivatedRoute,
    private productionService: ProductionService,
    private modalService: NgbModal,
  ) {}

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }

  validerProd() {
    this.productionService.update(this.production!).subscribe({
      next: res => {
        this.production = res.body!;
      },
    });
  }

  showModal() {
    const modalRef = this.modalService.open(ProductionUpdateComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.production = this.production;
    modalRef.result.then(rs => {
      if (rs === 'success') {
        {
          this.previousState();
        }
      }
    });
  }

  valideReception() {
    this.production!.etat = ETATPRODUCTION.TERMINER;
    this.productionService.partialUpdate(this.production!).subscribe({
      next: () => this.previousState(),
    });
  }
}
