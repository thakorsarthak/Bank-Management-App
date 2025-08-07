import { Routes } from '@angular/router';
import { PublicMainComponent } from './component/layout/public-main/public-main.component';


import { OpenAccountComponent } from './core-component/core-component/open-account/open-account.component';
import { PublicFooterComponent } from './core-component/core-component/public-footer/public-footer.component';
import { CoreComponentComponent } from './core-component/core-component/core-component.component';
import { LoginPageComponent } from './component/layout/login-page/login-page.component';

export const routes: Routes = [
   
  { path: '',
    loadChildren: () => 
      import('./core-component/core-component/core-component.module')
    .then(m => m.CoreComponentModule)
   },
  //{ path: 'openAccount', component: OpenAccountComponent },
 // { path: 'loginPage', component: LoginPageComponent },
  { path: '**', redirectTo: '' }
  

];
