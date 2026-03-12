import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Button } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { MessageModule } from 'primeng/message';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { ProgressBarModule } from 'primeng/progressbar';
import { ChartModule } from 'primeng/chart';

import { MessageService } from 'primeng/api';

import { OverlayBadgeModule } from 'primeng/overlaybadge';
import { AuthServiceService } from '../../core-component/services/auth-service.service';
import { TransactionService } from '../../core-component/services/transaction.service';


@Component({
  selector: 'app-dashboard',
  imports: [RouterLink, MessageModule, Button, CardModule, TableModule, TagModule, ProgressBarModule, ChartModule, OverlayBadgeModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
  providers: [MessageService]
})
export class DashboardComponent implements OnInit {
  account: any;
  totalTransactions = 0;
  creditTransactions = 0;
  debitTransactions = 0;

  constructor(
    private authservice: AuthServiceService,
    private messageService: MessageService,
    private transactionService: TransactionService
  ) { }
  ngOnInit(): void {

    console.log('dashboard component');
    this.transactionService.getAccountHolderDetails().subscribe(data => {
      this.account = data;
      this.transactionService.getTransactionHistory().subscribe({
        next: (res) => {
          const transactions = res.data; // assuming GlobalAPIResponse<Transaction[]>

          this.totalTransactions = transactions.length;
          this.creditTransactions = transactions.filter(t => t.direction === 'CREDIT').length;
          this.debitTransactions = transactions.filter(t => t.direction === 'DEBIT').length;
        },
        error: (err) => {
          console.error('Failed to fetch transactions:', err);
        }
      });
    });
  }

}
