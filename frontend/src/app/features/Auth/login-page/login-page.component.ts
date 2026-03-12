import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';
import { RippleModule } from 'primeng/ripple';
import { ToastModule } from 'primeng/toast';

import { MessageService } from 'primeng/api';
import { Password, PasswordModule } from 'primeng/password';
import { Router, RouterLink } from '@angular/router';

import { Dialog } from 'primeng/dialog';
import { PublicHeaderComponent } from '../../../shared/header/public-header/public-header.component';
import { AccountService } from '../../../core-component/services/account.service';
import { AuthServiceService } from '../../../core-component/services/auth-service.service';

@Component({
  selector: 'app-login-page',
  imports: [CommonModule, RouterLink, Dialog,
    ReactiveFormsModule, ToastModule, RippleModule, PublicHeaderComponent,
    InputTextModule, PasswordModule,
    DropdownModule,
    ButtonModule,
    CardModule,
    MessageModule, PublicHeaderComponent],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css',
  providers: [MessageService]
})
export class LoginPageComponent {

  loginForm!: FormGroup;


  AccountService = inject(AccountService);
  authService = inject(AuthServiceService);


  constructor(private fb: FormBuilder, private messageService: MessageService, private router: Router) { }


  ngOnInit() {
    this.loginForm = this.fb.group({
      identifier: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.pattern('^[A-Za-z0-9@$!%*?&]{4,12}$')]]
    },);
  }
  
  visible: boolean = false;

  showDialog() {
    this.visible = true;
  }

  closeDialog() {
    this.visible = false;
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.loginForm.get(fieldName);
    return field ? field.invalid && (field.dirty || field.touched) : false;
  }



  onSubmit() {
    if (this.loginForm.valid) {

      const loginData = this.loginForm.value;

      this.AccountService.loginAccount(loginData).subscribe({
        next: (res) => {
          
          //store token and decode in authservice
          this.authService.login(res.token, res.expiresAt);
          
          const role = this.authService.getRole();
          console.log("User role after login:", role);

         
          this.messageService.add({
            severity: 'success',
            summary: 'Success',
            detail: 'Login successful! Redirecting to dashboard...'
          });
          setTimeout(() => {
            if(role === 'ADMIN'){
              console.log("Navigating to admin dashboard");
              this.router.navigate(['privateMain/adminDashboard']);
            }
            if(role === 'EMPLOYEE'){
              this.router.navigate(['privateMain/employeeDashboard']);
            }
            if(role === 'USER') 
            this.router.navigate(['privateMain/dashBoard']);
          }, 1000);
        },
        error: (err: any) => {
          console.error('Login failed', err);
          let errorMsg = 'Login failed. Servers are down Momentarily. Please try again later.';
          if (err.status === 401 || err.status === 403) {
            errorMsg = 'Invalid email or pin. Please check your credentials.';
          }
          this.messageService.add({ 
            severity: 'error',
            summary: 'Login Failed',
            detail: errorMsg
          });
        }
      });
    }
  }
}







