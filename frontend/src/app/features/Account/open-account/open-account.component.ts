import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { MessageModule } from 'primeng/message';
import { ToastModule } from 'primeng/toast';
import { RippleModule } from 'primeng/ripple';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import { DialogModule } from 'primeng/dialog';
import { PublicHeaderComponent } from '../../../shared/header/public-header/public-header.component';
import { AccountService } from '../../../core-component/services/account.service';


@Component({
  selector: 'app-open-account',
  imports: [CommonModule, PublicHeaderComponent,
    ReactiveFormsModule, ToastModule, RippleModule, DialogModule,
    InputTextModule,
    DropdownModule,
    ButtonModule,
    CardModule,
    MessageModule, PublicHeaderComponent],
  templateUrl: './open-account.component.html',
  styleUrl: './open-account.component.css',
  providers: [MessageService]
})
export class OpenAccountComponent implements OnInit {

  signupForm!: FormGroup;
  showConfirmationDialog = false;

  AccountService = inject(AccountService);

  accountTypes = [
    { label: 'Savings Account', value: '01' },
    { label: 'Current Account', value: '02' },
    { label: 'Student Account', value: '03' },
    { label: 'Senior Citizen Account', value: '04' },
    { label: 'Salary Account', value: '05' },

  ];

  branches = [
    { label: 'Ahmedabad', value: '1001' },
    { label: 'Surat', value: '1002' },
    { label: 'Mumbai', value: '1003' },
  ];

  constructor(
    private fb: FormBuilder,
    private messageService: MessageService,
    private router: Router
  ) { }


  ngOnInit() {
    this.signupForm = this.fb.group({
      accountHolderName: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],

      // Login PIN password
      password: ['', [Validators.required, Validators.pattern('^[A-Za-z0-9@$!%*?&]{4,12}$')]],
      confirmPassword: ['', [Validators.required]],


      //transaction PIN 
      pin: ['', [Validators.required, Validators.pattern('^[0-9]{4,6}$')]],
      confirmPin: ['', [Validators.required]],

      contact: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],

      // KYC
      panNo: ['', [Validators.required, Validators.pattern('^[A-Z]{5}[0-9]{4}[A-Z]{1}$')]], // PAN format
      aadharNo: ['', [Validators.required, Validators.pattern('^[0-9]{12}$')]],
      branch: ['', Validators.required],
      accountType: ['', Validators.required],
      balance: [1000.0] ,

      address : this.fb.group({
      street: ['', [Validators.required, Validators.minLength(3)]],
      city: ['', [Validators.required, Validators.minLength(2)]],
      state: ['', [Validators.required, Validators.minLength(2)]],
      postalCode: ['', [Validators.required, Validators.pattern('^[0-9]{6}$')]]
    }),

    }, { validators: [this.matchTransactionPinValidator, this.matchPasswordValidator] });
  }

  // Validate Login PIN and Confirm PIN
  matchPasswordValidator(group: FormGroup) {
    const password = group.get('password')?.value;
    const confirmPassword = group.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMisMatch: true };
  }

  matchTransactionPinValidator(group: AbstractControl): ValidationErrors | null {
    const pin = group.get('pin')?.value;
    const confirmPin = group.get('confirmPin')?.value;
    return pin === confirmPin ? null : { TransactionPinMisMatch: true };
  }

  
  onCancelConfirmation() {
    this.showConfirmationDialog = false;
  }

  getBranchLabel(value: string): string {
    const branch = this.branches.find(branch => branch.value === value);
    return branch ? branch.label : value;
  }


  onCreateAccount() {
    if (this.signupForm.valid ) {
      this.showConfirmationDialog = true;
    } else {
      Object.keys(this.signupForm.controls).forEach(key => {
        const control = this.signupForm.get(key);
        if (control?.invalid) control.markAsTouched();
      });
    }
  }

  onSubmit() {
    if (this.signupForm.valid) {
      const formData = this.signupForm.value;

      // Map to backend structure
      formData.branchCode = formData.branch;
      formData.productCode = formData.accountType;

      this.AccountService.createAccount(formData).subscribe({
        next: (res) => {
          this.messageService.add({
            severity: 'success',
            summary: 'Registration Successful',
            detail: 'Please log in to continue'
          });
          setTimeout(() => this.router.navigate(['login']), 1000);
        },
        error: (err) => {
          console.log(err, "error");
          if (err.error?.data) {
            err.error.data.forEach((fieldError: any) => {
              if (fieldError.field === 'email') {
                this.messageService.add({ severity: 'error', summary: `${fieldError.field}`, detail: `${fieldError.message}` });
                this.signupForm.controls['email'].setErrors({ serverError: fieldError.message });
              }
              if (fieldError.field === 'contact') {
                this.messageService.add({ severity: 'error', summary: `${fieldError.field}`, detail: `${fieldError.message}`  });
                this.signupForm.controls['contact'].setErrors({ serverError: fieldError.message });
              }
              if (fieldError.field === 'aadharNo') {
                this.messageService.add({ severity: 'error', summary: `${fieldError.field}`, detail: `${fieldError.message}` });
                this.signupForm.controls['aadharNo'].setErrors({ serverError: fieldError.message });
              }
               if (fieldError.field === 'panNo') {
                this.messageService.add({ severity: 'error', summary: `${fieldError.field}`, detail: `${fieldError.message}` });
                this.signupForm.controls['panNo'].setErrors({ serverError: fieldError.message });
              }
            });
          }
        }
      });
    }
  }

  getAccountTypeLabel(value: string): string {
    const accountType = this.accountTypes.find(type => type.value === value);
    return accountType ? accountType.label : value;
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.signupForm.get(fieldName);
    return field ? field.invalid && (field.dirty || field.touched) : false;
  }

  getErrorMessage(fieldName: string): string {
    const control = this.signupForm.get(fieldName);
    if (control?.errors) {
      if (control.errors['required']) return 'This field is required';
      if (control.errors['email']) return 'Enter a valid email';
      if (control.errors['minlength']) return 'Minimum 3 characters';
      if (control.errors['panNo']) return 'Pan Number is Required';
      if (control.errors['pattern']) {
        if (fieldName === 'confirmPin') return 'PIN must be 4 to 6 digits';
        if (fieldName === 'contact') return 'Contact must be 10 digits';
        if (fieldName === 'panNo') return 'PAN number must be in format: ABCDE1234F';
      }
      if (control.errors['serverError']) return control.errors['serverError'];
    }
    if (fieldName === 'confirmPin' && this.signupForm.errors?.['TransactionPinMisMatch']) {
      return 'Transaction PIN do not match';
    }
    if (fieldName === 'confirmPassword' && this.signupForm.errors?.['passwordMisMatch']) {
      return 'Login Password do not match';
    }
    return '';
  }
}

