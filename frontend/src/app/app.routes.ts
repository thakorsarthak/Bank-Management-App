import { Routes } from '@angular/router';



export const routes: Routes = [
   
  { path: '',
    loadChildren: () => 
      import('./core-component/core-component.module')
    .then(m => m.CoreComponentModule)
   },
  //{ path: 'openAccount', component: OpenAccountComponent },
 // { path: 'loginPage', component: LoginPageComponent },
  { path: '**', redirectTo: '' }
  

];
