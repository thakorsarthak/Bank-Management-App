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
import { AccountService } from '../../services/account.service';
import { MessageService } from 'primeng/api';
import { PasswordModule } from 'primeng/password';
import { Router, RouterLink } from '@angular/router';
import { AuthServiceService } from '../../services/auth-service.service';
import { PublicHeaderComponent } from "../../../core-component/core-component/public-header/public-header.component";
import { Dialog } from 'primeng/dialog';

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
      pin: ['', [Validators.required, Validators.pattern('^[0-9]{4,6}$')]]
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
          this.authService.login(res.token, res.expiresAt);
          const token = res.token;
          const expiresAt = Date.now() + 60 * 60 * 1000; // 10 minutes

          localStorage.setItem('token', token);
          localStorage.setItem('expiresAt', expiresAt.toString());
          this.messageService.add({
            severity: 'success',
            summary: 'Success',
            detail: 'Login successful! Redirecting to dashboard...'
          });
          setTimeout(() => {
            this.router.navigate(['privateMain']);
          }, 1000);
        },
        error: (err: any) => {
          console.error('Login failed', err);
          let errorMsg = 'Login failed. Please try again.';
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







