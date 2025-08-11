import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Button } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { MessageModule } from 'primeng/message';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { ProgressBarModule } from 'primeng/progressbar';
import { ChartModule } from 'primeng/chart';
import { AuthServiceService } from '../../services/auth-service.service';
import { MessageService } from 'primeng/api';
import { TransactionService } from '../../services/transaction.service';


@Component({
  selector: 'app-dashboard',
  imports: [RouterLink,MessageModule, Button, CardModule, TableModule, TagModule, ProgressBarModule,ChartModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
  providers: [MessageService]
})
export class DashboardComponent implements OnInit{
 account: any;

  constructor(
     private authservice: AuthServiceService,
    private messageService: MessageService,
    private transactionService: TransactionService
  ) { }
   ngOnInit(): void {

    console.log('dashboard component');
    this.transactionService.getAccountHolderDetails().subscribe(data => {
      this.account = data;
      });
  //     this.account = data;
    //decoding token to get account number
  //   const accountNumber = this.authservice.getAccountNumber(); // Or get from JWT or route
  //   console.log('Account Number:', accountNumber);
  //   if (accountNumber) {
  //   this.transactionService.getAccountHolderDetails(accountNumber).subscribe(data => {
  //     this.account = data;
  //   });
  // } else {
  //   // Handle the case where accountNumber is null (e.g., show an error or redirect)
  //   this.messageService.add({
  //     severity: 'error',
  //     summary: 'Account Error',
  //     detail: 'No account number found.'
  //   });
  // }



}
}
