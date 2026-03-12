import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { MenubarModule } from 'primeng/menubar';
import { ButtonModule } from 'primeng/button';
import { MenuItem } from 'primeng/api';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
@Component({
  selector: 'app-public-header',
  imports: [ CommonModule,RouterLink,
    MenubarModule,
    ButtonModule,
    DialogModule,
    InputTextModule,
    PasswordModule,
    FormsModule],
  templateUrl: './public-header.component.html',
  styleUrl: './public-header.component.css'
})
export class PublicHeaderComponent  implements OnInit{
items: MenuItem[] = [];
  loginVisible: boolean = false;
  signupVisible: boolean = false;
  username: string = '';
  password: string = '';
  fullName: string = '';
  email: string = '';

//  router =inject(Router)
//  onopenAccount(){
//   console.log('open account');
//   this.router.navigateByUrl('/open-account')
//  }

  ngOnInit() {
    this.items = [
      {
        label: 'Personal',
        icon: 'pi pi-user',
        items: [
          { label: 'Checking', icon: 'pi pi-wallet' },
          { label: 'Savings', icon: 'pi pi-money-bill' },
          { label: 'Credit Cards', icon: 'pi pi-credit-card' },
          { label: 'Loans', icon: 'pi pi-percentage' }
        ]
      },
      {
        label: 'Business',
        icon: 'pi pi-briefcase',
        items: [
          { label: 'Business Checking', icon: 'pi pi-wallet' },
          { label: 'Merchant Services', icon: 'pi pi-shopping-cart' },
          { label: 'Business Loans', icon: 'pi pi-percentage' },
          { label: 'Treasury Management', icon: 'pi pi-chart-line' }
        ]
      },
      {
        label: 'Investments',
        icon: 'pi pi-chart-bar'
      },
      {
        label: 'Resources',
        icon: 'pi pi-info-circle'
      }
    ];
  }

  showLoginDialog() {
    this.loginVisible = true;
  }

  showSignupDialog() {
    this.signupVisible = true;
  }
}