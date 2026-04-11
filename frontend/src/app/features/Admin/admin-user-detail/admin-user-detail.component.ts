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
  failedTransactions = 0;
  successTransactions = 0;

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
    
        this.adminservice.getTransOveriewCard(Number(this.accountId), {}  ).subscribe({
           next: (data : any) => {
           
             
            //  this.totalTransactions = data.successTransaction;
              this.totalTransactions = data.total;
              this.creditTransactions = data.credited;
              this.debitTransactions = data.debited;
              this.failedTransactions = data.failed;
              this.successTransactions = data.success;

            
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
