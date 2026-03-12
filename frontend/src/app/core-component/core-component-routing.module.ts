import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CoreComponentComponent } from './core-component.component';
import { PublicMainComponent } from '../layouts/public-main/public-main.component';
import { ForgetPinComponent } from '../features/Auth/forget-pin/forget-pin.component';
import { ResetPasswordComponent } from '../features/Auth/reset-password/reset-password.component';
import { OpenAccountComponent } from '../features/Account/open-account/open-account.component';
import { LoginPageComponent } from '../features/Auth/login-page/login-page.component';
import { DashboardComponent } from '../features/dashboard/dashboard.component';
import { TransactionComponent } from '../features/transaction/transaction.component';
import { TransferMoneyComponent } from '../features/transfer-money/transfer-money.component';
import { PrivateMainComponent } from '../layouts/private-main/private-main.component';
import { authGuardGuard } from './guards/auth-guard.guard';
import { roleGuard } from './guards/role.guard';
import { AdminComponentComponent } from '../features/Admin/admin-component/admin-component.component';



const routes: Routes = [
  {
    path: '',
    component: CoreComponentComponent,
    children: [
      { path: '', component: PublicMainComponent },
      { path: 'forgetPin', component: ForgetPinComponent },
      { path: 'openAccount', component: OpenAccountComponent },
      { path: 'login', component: LoginPageComponent },
      { path: 'resetPassword', component: ResetPasswordComponent },
    ]
  },

  {
    path: 'privateMain',
    component: PrivateMainComponent,
    canActivate: [authGuardGuard],
    children: [

      // COMMON (logged-in users only)
      {
        path: 'forgetPin',
        component: ForgetPinComponent
      },

      // USER ONLY
      {
        path: 'dashBoard',
        component: DashboardComponent,
        canActivate: [roleGuard],
        data: { roles: ['USER'] }
      },
      {
        path: 'transferMoney',
        component: TransferMoneyComponent,
        canActivate: [roleGuard],
        data: { roles: ['USER'] }
      },
      {
        path: 'transaction',
        component: TransactionComponent,
        canActivate: [roleGuard],
        data: { roles: ['USER'] }
      },

      // ADMIN ONLY
      {
        path: 'adminDashboard',
        component: AdminComponentComponent,
        canActivate: [roleGuard],
        data: { roles: ['ADMIN'] }
      }
    ]
  }
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CoreComponentRoutingModule { }
