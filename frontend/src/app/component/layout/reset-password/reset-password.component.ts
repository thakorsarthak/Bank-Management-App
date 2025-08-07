import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { ToastModule } from 'primeng/toast';
import { AccountService } from '../../services/account.service';
import { DialogModule } from 'primeng/dialog';

@Component({
  selector: 'app-reset-password',
  imports: [RouterLink, CommonModule, DialogModule,
    ReactiveFormsModule,
    ToastModule,
    ButtonModule,
    CardModule,
    InputTextModule],
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.css',
  providers: [MessageService]
})
export class ResetPasswordComponent implements OnInit, OnDestroy {
  resetForm!: FormGroup;
  pinForm!: FormGroup;

  pinDialogVisible = false;

  isOtpSent = false;
  resendDisabled = false;
  verifyDisabled = true;

  otpValidSeconds = 300;
  timeLeft = this.otpValidSeconds;
  intervalId: any;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private messageService: MessageService,
    private accountsService: AccountService
  ) {}

  ngOnInit() {
    this.resetForm = this.fb.group({
      identifier: ['', [Validators.required, this.emailOrPhoneValidator]],
      otp: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]]
    });
    // Initialize pin form -- p daialog
     this.pinForm = this.fb.group({
    newPin: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(8)]],
    confirmPin: ['', Validators.required]
  }, { validator: this.matchPins });
  }

  //match the new pin and confirm pin
  matchPins(group: FormGroup) {
  const pin = group.get('newPin')?.value;
  const confirmPin = group.get('confirmPin')?.value;
  return pin === confirmPin ? null : { notMatching: true };
}

  onPinSubmit() {
    if(this.pinForm.invalid) return;
    const{newPin, confirmPin} = this.pinForm.value;
    const identifier = this.resetForm.get('identifier')?.value?.trim();

    const payload : any = {newPin, confirmPin};
    if(/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(identifier)) {
      payload.email = identifier;
    }else if (/^[0-9]{10}$/.test(identifier)) {
    payload.contact = `91${identifier}`;     //json for api
  }

  this.accountsService.setPinWithOtp(payload).subscribe({
      next: () => {
        this.messageService.add({
        severity: 'success',
        summary: 'PIN Updated',
        detail: 'Your PIN has been successfully updated. You may Login now.'
      });

      // Reset forms and close dialog
      this.pinDialogVisible = false;
      this.pinForm.reset();
      this.resetForm.reset();
      

      // Reset OTP state
     this.isOtpSent = false;
  this.verifyDisabled = true;
  this.resendDisabled = false;

      
      // Reset timer and  hide timer
  this.stopTimer();
  this.timeLeft = this.otpValidSeconds;

    },
    error: () => {
      this.messageService.add({
        severity: 'error',
        summary: 'Failed',
        detail: 'Failed to update PIN. Please try again.'
      });
    }
  });
  }


  // Validator for email or 10-digit phone
  emailOrPhoneValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return { required: true };
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const phonePattern = /^[0-9]{10}$/;
    return emailPattern.test(value) || phonePattern.test(value)
      ? null
      : { emailOrPhone: true };
  }


  onSendOrResendOtp() {
    if (this.resendDisabled) return;

    const identifier = this.resetForm.get('identifier')?.value?.trim();

    if (!identifier) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Input Required',
        detail: 'Please enter your email or phone number.'
      });
      return;
    }

    // Distinguish email or phone
    const isEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(identifier);
    const isPhone = /^[0-9]{10}$/.test(identifier);

    if (!isEmail && !isPhone) {
      this.messageService.add({
        severity: 'error',
        summary: 'Invalid Input',
        detail: 'Enter a valid email or 10-digit phone number.'
      });
      return;
    }

    // Send to backend
    const payload: any = {};
    if (isEmail) payload.email = identifier;
    if (isPhone) payload.phone = `+91${identifier}`;

  
  
  this.resendDisabled = true;

    this.accountsService.sendOtp(payload).subscribe({
      next: () => {
        this.isOtpSent = true;
     
        this.messageService.add({
          severity: 'info',
          summary: 'OTP Sent',
          detail: `OTP has been sent to your ${isEmail ? 'email' : 'phone'}.`
        });
        this.timeLeft = this.otpValidSeconds;
        this.startTimer();
      },
      error: (err) => {
        const errorMessage = err?.error?.message || 'Could not send OTP. Try again later.';
        this.messageService.add({
          severity: 'error',
          summary: 'Failed',
          detail: errorMessage
        });
       this.resendDisabled = false;
     
      this.isOtpSent = false;
      }
    });
  }

  onSubmitVerifyOtp() {
    if (this.resetForm.invalid) return;

    if (this.timeLeft <= 0) {
      this.messageService.add({
        severity: 'error',
        summary: 'Expired OTP',
        detail: 'OTP has expired. Please resend.'
      });
      return;
    }

    const identifier = this.resetForm.get('identifier')?.value?.trim();
    const otp = this.resetForm.get('otp')?.value?.trim();

    const isEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(identifier);
    const isPhone = /^[0-9]{10}$/.test(identifier);

    const payload: any = { otp };
    if (isEmail) payload.email = identifier;
    if (isPhone) payload.phone = `+91${identifier}`;

    this.accountsService.verifyOtp(payload).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'OTP Verified',
          detail: 'OTP is valid. You may proceed.'
        });
        this.verifyDisabled = true;
        // open reset Pin dialog POp-up
        this.pinDialogVisible = true;
        this.stopTimer();
      },
      error: () => {
        this.messageService.add({
          severity: 'error',
          summary: 'Invalid OTP',
          detail: 'Incorrect OTP. Please try again.'
        });
      }
    });
  }

  startTimer() {
    this.stopTimer();
    this.intervalId = setInterval(() => {
      this.timeLeft--;
      if (this.timeLeft <= 0) {
        this.resendDisabled = false;
        this.verifyDisabled = true;
        this.stopTimer();
      }
    }, 1000);
  }

  stopTimer() {
    if (this.intervalId) {
      clearInterval(this.intervalId);
      this.intervalId = null;
    }
  }

  get timeLeftDisplay(): string {
    const min = Math.floor(this.timeLeft / 60);
    const sec = this.timeLeft % 60;
    return `${this.pad(min)}:${this.pad(sec)}`;
  }

  pad(num: number): string {
    return num < 10 ? '0' + num : '' + num;
  }

  ngOnDestroy(): void {
    this.stopTimer();
  }
}
// This component handles the reset password functionality, 
// including OTP generation and verification.


