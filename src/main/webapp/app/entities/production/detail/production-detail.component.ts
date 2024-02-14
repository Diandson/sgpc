import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { DataUtils } from 'app/core/util/data-util.service';
import { IProduction } from '../production.model';
import { Authority } from '../../../config/authority.constants';
import { NgIf } from '@angular/common';
import HasAnyAuthorityDirective from '../../../shared/auth/has-any-authority.directive';

@Component({
  standalone: true,
  selector: 'jhi-production-detail',
  templateUrl: './production-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, NgIf, HasAnyAuthorityDirective],
})
export class ProductionDetailComponent {
  @Input() production: IProduction | null = null;
  auth = Authority;

  constructor(
    protected dataUtils: DataUtils,
    protected activatedRoute: ActivatedRoute,
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
}
