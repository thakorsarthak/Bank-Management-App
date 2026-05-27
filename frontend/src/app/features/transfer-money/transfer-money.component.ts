import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';

import { CommonModule } from '@angular/common';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { MessageModule } from 'primeng/message';
import { ToastModule } from 'primeng/toast';
import { debounceTime, distinctUntilChanged, filter } from 'rxjs';
import { PrivateHeaderComponent } from '../../shared/header/private-header/private-header.component';
import { AuthServiceService } from '../../core-component/services/auth-service.service';
import { TransactionService } from '../../core-component/services/transaction.service';
import { HttpHeaders } from '@angular/common/http';


@Component({
  selector: 'app-transfer-money',
  imports: [CommonModule,
    ReactiveFormsModule,
    InputTextModule,
    InputNumberModule,
    ButtonModule,
    CardModule,
    MessageModule,
    ToastModule,
    PrivateHeaderComponent],
  templateUrl: './transfer-money.component.html',
  styleUrls: ['./transfer-money.component.css'],
  providers: [MessageService]
})
export class TransferMoneyComponent implements OnInit {

  constructor(
    private fb: FormBuilder,
    private authservice: AuthServiceService,
    private transactionService: TransactionService,
    private messageService: MessageService
  ) { }


  transactionForm!: FormGroup;

  receiverName: string | null = null;

  ngOnInit(): void {
    this.transactionForm = this.fb.group({
      accountNumber: ['', [Validators.required, Validators.pattern(/^\d{12}$/)]],
      amount: [null, [Validators.required, Validators.min(1), Validators.max(50000)]],
      pin: ['', [Validators.required, Validators.pattern(/^\d{4,6}$/)]],
      description: ['']
    });

    this.transactionForm.get('accountNumber')?.valueChanges.pipe(
      debounceTime(500),
      distinctUntilChanged(),
      filter(value => /^\d{12}$/.test(value))
    )
      .subscribe(accountNumber => {
        this.transactionService.getAccountHolderName(accountNumber).subscribe({
          next: (res) => {
            this.receiverName = res.success ? res.data : null;
          },
          error: () => {
            this.receiverName = null;
          }
        });
      });
  }

  isFieldInvalid(field: string): boolean {
    const control = this.transactionForm.get(field);
    return !!(control && control.invalid && (control.dirty || control.touched));
  }

  getErrorMessage(field: string): string {
    const control = this.transactionForm.get(field);
    if (!control) return '';

    if (control.errors?.['required']) return `${this.formatFieldName(field)} is required`;
    if (control.errors?.['pattern']) return `Invalid ${this.formatFieldName(field)} format`;
    if (control.errors?.['min']) return `Minimum amount is ₹1`;
    if (control.errors?.['max']) return `Maximum allowed is ₹50,000`;

    return '';
  }

  formatFieldName(field: string): string {
    return field === 'pin' ? 'PIN' : field === 'amount' ? 'Amount' : 'Account Number';
  }

  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('en-IN', {
      style: 'currency',
      currency: 'INR'
    }).format(amount);
  }

  onCancel(): void {
    this.transactionForm.reset();
  }

  isProcessing = false;

  onSubmit(): void {
    if (this.transactionForm.invalid) return;

    // Prevent double click
    if (this.isProcessing) {
      return;
    }

    const headers = new HttpHeaders({
      'Idempotency-Key': crypto.randomUUID()
    });


    const payload = {
      toAccountNumber: this.transactionForm.value.accountNumber,
      amount: this.transactionForm.value.amount,
      pin: this.transactionForm.value.pin,
      description: this.transactionForm.value.description || null
    };

    this.isProcessing = true;

    this.transactionService.transferMoney(payload, { headers }).subscribe({
      next: () => {
        this.isProcessing = false;
        this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Money transferred successfully' });
        this.transactionForm.reset();
        this.receiverName = null;
      },
      error: (err) => {
        this.isProcessing = false;
        this.messageService.add({ severity: 'error', summary: 'Error', detail: err.error?.message || 'Transfer failed' });
      }
    });
  }
}