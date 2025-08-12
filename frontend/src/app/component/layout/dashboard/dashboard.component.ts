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
  imports: [RouterLink, MessageModule, Button, CardModule, TableModule, TagModule, ProgressBarModule, ChartModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
  providers: [MessageService]
})
export class DashboardComponent implements OnInit {
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
  }
}
