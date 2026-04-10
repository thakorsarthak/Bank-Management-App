import { Component } from '@angular/core';
import { ActivatedRoute, Router, RouterLink, RouterOutlet } from '@angular/router';
import { AdminService } from '../../../core-component/services/admin.service';
import { TransactionService } from '../../../core-component/services/transaction.service';

@Component({
  selector: 'app-admin-user-detail',
  imports: [RouterOutlet, RouterLink],
  templateUrl: './admin-user-detail.component.html',
  styleUrl: './admin-user-detail.component.css'
})
export class AdminUserDetailComponent {

 accountId: string | null = null;
 account : any;
totalTransactions = 0;
  creditTransactions = 0;
  debitTransactions = 0;

  constructor(
  private route: ActivatedRoute,
  private adminservice: AdminService,
  private transactionService: TransactionService,
) {}

  ngOnInit() {
    console.log('AdminUserDetailComponent initialized');
    this.accountId = this.route.snapshot.paramMap.get('id');
    console.log('Extracted accountId:', this.accountId);
     this.adminservice.viewUser(Number(this.accountId), {}).subscribe({
       next: (data) => {
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
       },
       error: (err) => {
         console.error('Failed to fetch user:', err);
       }
     });

  }

}
