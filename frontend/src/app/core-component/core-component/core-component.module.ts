import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CoreComponentRoutingModule } from './core-component-routing.module';
import { CoreComponentComponent } from './core-component.component';
import { OpenAccountComponent } from './open-account/open-account.component';
import { PublicFooterComponent } from './public-footer/public-footer.component';
import { PublicHeaderComponent } from './public-header/public-header.component';
import { PublicMainComponent } from '../../component/layout/public-main/public-main.component';
import { LoginPageComponent } from '../../component/layout/login-page/login-page.component';



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
