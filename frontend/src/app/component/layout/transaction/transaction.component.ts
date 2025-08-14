import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterOutlet } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { PaginatorModule } from 'primeng/paginator';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { PrivateHeaderComponent } from '../../../core-component/core-component/private-header/private-header.component';
import { TransactionService } from '../../services/transaction.service';
import { Transaction } from '../../Models/Transaction';
import { GlobalAPIResponse } from '../../Models/global-api-response';
import { AuthServiceService } from '../../services/auth-service.service';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import {  ConfirmPopupModule } from 'primeng/confirmpopup';
import {  ToastModule } from 'primeng/toast';
import { Dialog } from 'primeng/dialog';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'app-transaction',
  standalone: true,
  imports: [
    RouterOutlet,
    CommonModule,
    FormsModule,
    ConfirmPopupModule,ToastModule,ConfirmDialogModule,
    CardModule,
    TableModule,
    TagModule,
    CalendarModule,
    ButtonModule,
    InputTextModule,
    DropdownModule,
    PaginatorModule,
    PrivateHeaderComponent
  ],
  templateUrl: './transaction.component.html',
  styleUrl: './transaction.component.css',
  providers: [ConfirmationService, MessageService]
})
export class TransactionComponent implements OnInit {


  fromDate: Date | null = null;
  toDate: Date | null = null;
  maxFromDate: Date = new Date();
  maxToDate: Date = new Date();
  showValidation: boolean = false;
  isDownloading: boolean = false;


  transactions: Transaction[] = [];
  filteredTransactions: Transaction[] = [];

  // Filter states
  searchTerm = '';
  selectedType: string = '';
  selectedStatus: string = '';
  dateFrom: Date | null = null;
  dateTo: Date | null = null;
  direction: string = '';// 'all', 'credit', 'debit'

  showFilters = true;

  // Dropdown options
  typeOptions = [
    { label: 'All Types', value: '' },
    { label: 'Credit', value: 'credit' },
    { label: 'Debit', value: 'debit' },
  ];

  statusOptions = [
    { label: 'All Status', value: '' },
    { label: 'Completed', value: 'completed' },
    { label: 'Failed', value: 'failed' },
    { label: 'Cancelled', value: 'cancelled' }
  ];

  constructor(
    private router: Router,
    private transactionService: TransactionService,
    private authService: AuthServiceService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
  this.loadTransactions();
  }
 // Open Confirm Dialog
  openDownloadDialog() {
    this.confirmationService.confirm({
      key: 'customConfirm',
      message: '' // Required for opening, but actual content comes from template
    });
  }

  // Cancel
  cancel() {
    this.fromDate = null;
    this.toDate = null;
    this.confirmationService.close();
  }

  // Date Range Validation
  isValidDateRange(): boolean {
    if (!this.fromDate || !this.toDate) return false;
    if (this.toDate < this.fromDate) return false;

    const diffInDays =
      (this.toDate.getTime() - this.fromDate.getTime()) / (1000 * 3600 * 24);
    return diffInDays <= 90; // Max 90 days range
  }

  // Call Download API
  downloadExcel() {
    if (!this.isValidDateRange()) {
      this.showValidation = true;
      return;
    }

    this.isDownloading = true;

    const from = this.formatDate(this.fromDate!);
    const to = this.formatDate(this.toDate!);

    this.transactionService.getTransactionExcelHistoryByDate(from, to).subscribe({
      next: (res: Blob) => {
        const url = window.URL.createObjectURL(res);
        const a = document.createElement('a');
        a.href = url;
        a.download = `transactions_${from}_to_${to}.xlsx`;
        a.click();
        window.URL.revokeObjectURL(url);
        this.isDownloading = false;
        this.confirmationService.close();
        this.messageService.add({ severity: 'success', summary: 'Download Started', detail: 'Excel file is being downloaded.' });
      },
      error: () => {
        this.isDownloading = false;
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to download Excel file.' });
      }
    });
  }

  // Helper to format date to yyyy-MM-dd
  private formatDate(date: Date): string {
    return date.toISOString().split('T')[0];
  }



  loadTransactions(): void {
    this.transactionService.getTransactionHistory().subscribe({
      next: (response) => {
        this.transactions = Array.isArray(response.data) ? response.data : [];
        this.applyFilters();
      },
      error: (err) => {
        console.error('Failed to fetch transactions', err);
      }
    });
  }

  applyFilters(): void {
    const searchLower = this.searchTerm.trim().toLowerCase();

    this.filteredTransactions = this.transactions.filter(transaction => {
      const description = transaction.description?.toLowerCase() || '';
      const counterParty = transaction.counterPartyName?.toLowerCase() || '';
      const status = transaction.status?.toLowerCase() || '';
      const type = transaction.direction?.toLowerCase() || '';
      const txnDate = new Date(transaction.timestamp);


      const matchesSearch =
        !this.searchTerm ||
        description.includes(searchLower) ||
        counterParty.includes(searchLower);

  
      const matchesType = !this.direction || type === this.direction.toLowerCase();
      const matchesStatus = !this.selectedStatus || status === this.selectedStatus.toLowerCase();

      const matchesDateFrom = !this.dateFrom || txnDate >= this.normalizeDate(this.dateFrom);
      const matchesDateTo = !this.dateTo || txnDate <= this.normalizeDate(this.dateTo, true);

      return matchesSearch && matchesType && matchesStatus && matchesDateFrom && matchesDateTo;
    });
  }
  normalizeDate(date: Date, endOfDay = false): Date {
    const normalized = new Date(date);
    if (endOfDay) {
      normalized.setHours(23, 59, 59, 999);
    } else {
      normalized.setHours(0, 0, 0, 0);
    }
    return normalized;
  }


  clearFilters(): void {
    this.searchTerm = '';
    this.selectedType = '';
    this.selectedStatus = '';
    this.direction = '';
    this.dateFrom = null;
    this.dateTo = null;
    this.applyFilters();
  }

  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('en-IN', {
      style: 'currency',
      currency: 'INR'
    }).format(Math.abs(amount));
  }

  getTransactionSeverity(direction: string): 'success' | 'danger' {
    return direction?.toLowerCase() === 'credit' ? 'success' : 'danger';
  }

  getStatusSeverity(status: string): 'success' | 'warning' | 'danger' {
    switch (status?.toLowerCase()) {
      case 'completed': return 'success';
      case 'failed': return 'danger';
      case 'cancelled': return 'warning';
      default: return 'success';
    }
  }

  getStatusIcon(status: string): string {
    switch (status?.toLowerCase()) {
      case 'completed': return 'pi-check-circle';
      case 'failed': return 'pi-times-circle';
      case 'cancelled': return 'pi-ban';
      default: return 'pi-question-circle';
    }
  }

  toggleFilters(): void {
    this.showFilters = !this.showFilters;
  }

  getCreditCount(): number {
    return this.filteredTransactions.filter(t => t.direction?.toLowerCase() === 'credit').length;
  }

  getDebitCount(): number {
    return this.filteredTransactions.filter(t => t.direction?.toLowerCase() === 'debit').length;
  }

  refreshTransactions(): void {
    
      this.loadTransactions();
    
  }

  onBackToDashboard(): void {
    this.router.navigate(['/privateMain/dashBoard']);
  }

  onExportTransactions() {
  const doc = new jsPDF();

  
  // Title
  doc.text('Transaction History', 14, 15);

  // Define table columns
  const headers = [['Date', 'Description', 'Type', 'Amount', 'Status', 'From/To', 'After Balance']];

  // Prepare rows from filtered transactions
  const data = this.filteredTransactions.map(tx => [
    new Date(tx.timestamp).toLocaleString(),
    tx.description || '',
    tx.type,
    this.formatCurrency(tx.amount),
    tx.status,
    tx.counterPartyName || '',
    this.formatCurrency(tx.afterBalance)
  ]);

  // Add table to PDF
  autoTable(doc, {
    head: headers,
    body: data,
    startY: 20,
    styles: {
    fontSize: 10,
    cellPadding: 4,
    halign: 'center'
  },
  });

  // Save PDF file
  doc.save('transaction-history.pdf');
}
  // onExportTransactions(): void {
  //   const csvRows: string[] = [];
  //   csvRows.push('Date,Description,Type,Amount,Status,From/To,After Balance');

  //   for (const tx of this.filteredTransactions) {
  //     const row = [
  //       new Date(tx.timestamp).toLocaleString(),
  //       `"${tx.description}"`,
  //       tx.type,
  //       this.formatCurrency(tx.amount),
  //       tx.status,
  //       tx.counterPartyName || '',
  //       this.formatCurrency(tx.afterBalance)
  //     ];
  //     csvRows.push(row.join(','));
  //   }

  //   const blob = new Blob([csvRows.join('\n')], { type: 'text/csv' });
  //   const link = document.createElement('a');
  //   link.href = URL.createObjectURL(blob);
  //   link.download = 'transaction-history.csv';
  //   link.click();
  // }
}

