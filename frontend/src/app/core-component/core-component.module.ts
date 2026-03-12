import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CoreComponentRoutingModule } from './core-component-routing.module';
import { CoreComponentComponent } from './core-component.component';
import { OpenAccountComponent } from '../features/Account/open-account/open-account.component';
import { LoginPageComponent } from '../features/Auth/login-page/login-page.component';
import { PublicMainComponent } from '../layouts/public-main/public-main.component';
import { PublicFooterComponent } from '../shared/footer/public-footer/public-footer.component';
import { PublicHeaderComponent } from '../shared/header/public-header/public-header.component';




@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    CoreComponentRoutingModule,
    CoreComponentComponent,
    OpenAccountComponent,
    PublicFooterComponent,
    PublicHeaderComponent,
    PublicMainComponent,
    LoginPageComponent
    ]
})
export class CoreComponentModule { }
