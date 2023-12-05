import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { SimplebarAngularModule } from 'simplebar-angular';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';

import { LayoutComponent } from './layout.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { TopbarComponent } from './topbar/topbar.component';
import { FooterComponent } from './footer/footer.component';
import { VerticalComponent } from './vertical/vertical.component';
import { TranslateModule } from '@ngx-translate/core';
import SharedModule from '../../shared/shared.module';

@NgModule({
  // tslint:disable-next-line: max-line-length
  declarations: [LayoutComponent, SidebarComponent, TopbarComponent, FooterComponent, VerticalComponent],
  imports: [CommonModule, TranslateModule, RouterModule, BsDropdownModule.forRoot(), SharedModule, SimplebarAngularModule],
  exports: [LayoutComponent],
})
export class LayoutsModule {}
