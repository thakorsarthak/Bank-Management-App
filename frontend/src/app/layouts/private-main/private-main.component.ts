import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { MessageService } from 'primeng/api';
import { PrivateHeaderComponent } from '../../shared/header/private-header/private-header.component';
import { TransactionComponent } from '../../features/transaction/transaction.component';
import { AuthServiceService } from '../../core-component/services/auth-service.service';


@Component({
  selector: 'app-private-main',
  imports: [ PrivateHeaderComponent, RouterOutlet, RouterLink],
  templateUrl: './private-main.component.html',
  styleUrl: './private-main.component.css',
  providers: [MessageService]
})
export class PrivateMainComponent implements OnInit {
  constructor(

    private authservice: AuthServiceService,
    private messageService: MessageService,

  ) {}
  ngOnInit(): void {

  }
}
