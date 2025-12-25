import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CoreComponentComponent } from './core-component.component';
import { OpenAccountComponent } from './open-account/open-account.component';
import { PublicMainComponent } from '../../component/layout/public-main/public-main.component';
import { LoginPageComponent } from '../../component/layout/login-page/login-page.component';
import { ResetPasswordComponent } from '../../component/layout/reset-password/reset-password.component';
import { DashboardComponent } from '../../component/layout/dashboard/dashboard.component';
import { PrivateMainComponent } from '../../component/layout/private-main/private-main.component';
import { authGuardGuard } from '../../guards/auth-guard.guard';
import { TransferMoneyComponent } from '../../component/layout/transfer-money/transfer-money.component';
import { TransactionComponent } from '../../component/layout/transaction/transaction.component';
import { ForgetPinComponent } from '../../component/layout/forget-pin/forget-pin.component';
import { AdminComponentComponent } from '../../component/layout/admin-component/admin-component.component';
import { roleGuard } from '../../guards/role.guard';


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
