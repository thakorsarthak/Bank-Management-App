import { Component, OnInit } from '@angular/core';
import { DashboardComponent } from "../dashboard/dashboard.component";
import { PrivateHeaderComponent } from "../../../core-component/core-component/private-header/private-header.component";
import { RouterLink, RouterOutlet } from '@angular/router';
import { TransactionComponent } from '../transaction/transaction.component';
import { MessageService } from 'primeng/api';
import { AuthServiceService } from '../../services/auth-service.service';

@Component({
  selector: 'app-private-main',
  imports: [ PrivateHeaderComponent, RouterOutlet,RouterLink , TransactionComponent],
  templateUrl: './private-main.component.html',
  styleUrl: './private-main.component.css',
  providers: [MessageService]
})
export class PrivateMainComponent implements OnInit {
  constructor(

    private authservice: AuthServiceService,
    private messageService: MessageService
  ) {}
  ngOnInit(): void {



  }

  

}
