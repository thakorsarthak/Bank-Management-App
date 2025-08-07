import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { MenubarModule } from 'primeng/menubar';
import { PasswordModule } from 'primeng/password';
import { AuthServiceService } from '../../../component/services/auth-service.service';
import { AvatarModule } from 'primeng/avatar';
import { OverlayBadgeModule } from 'primeng/overlaybadge';

@Component({
  selector: 'app-private-header',
  imports: [CommonModule,RouterLink,AvatarModule, OverlayBadgeModule,
    MenubarModule,
    ButtonModule,
    DialogModule,
    InputTextModule,
    PasswordModule,
    FormsModule],
  templateUrl: './private-header.component.html',
  styleUrl: './private-header.component.css'
})
export class PrivateHeaderComponent {

 items: MenuItem[] = [];
  
  remainingTime: string = '';
  private timerInterval: any;

constructor(private authService: AuthServiceService, private router: Router) {}
  


  ngOnInit() {

    this.startCountdown();
    console.log('Expires at:', new Date(parseInt(localStorage.getItem('expiresAt') || '0', 10)));
    this.items = [
      {
        label: 'Home',
        icon: 'pi pi-home',
        command: () => {
          this.router.navigate(['privateMain']);
        }
      },
      {
        label: 'Accounts',
        icon: 'pi pi-wallet',
        items: [
          { 
            label: 'View Accounts', 
            icon: 'pi pi-eye',
            command: () => {
              this.router.navigate(['/privateMain/dashBoard']);
            }
          },
          { 
            label: 'Transfer Money', 
            icon: 'pi pi-arrow-right-arrow-left',
            command: () => {
              this.router.navigate(['/privateMain/transferMoney']);
              console.log('Transfer money clicked');
            }
          },
          { 
            label: 'Transaction History', 
            icon: 'pi pi-history',
            command: () => {
              this.router.navigate(['/privateMain/transaction']);
              console.log('Transaction history clicked');
            }
          }
        ]
      },
      {
        label: 'Services',
        icon: 'pi pi-cog',
        items: [
          { 
            label: 'Pay Bills', 
            icon: 'pi pi-file-edit',
            command: () => {
              console.log('Pay bills clicked');
            }
          },
          { 
            label: 'Deposit Check', 
            icon: 'pi pi-plus',
            command: () => {
              console.log('Deposit check clicked');
            }
          },
          { 
            label: 'Card Management', 
            icon: 'pi pi-credit-card',
            command: () => {
              console.log('Card management clicked');
            }
          }
        ]
      },
      {
        label: 'Support',
        icon: 'pi pi-question-circle',
        items: [
          { 
            label: 'Help Center', 
            icon: 'pi pi-info-circle',
            command: () => {
              console.log('Help center clicked');
            }
          },
          { 
            label: 'Contact Us', 
            icon: 'pi pi-phone',
            command: () => {
              console.log('Contact us clicked');
            }
          }
        ]
      }
    ];
  }

  onLogout() {
    // Clear user session and token here
  
  this.authService.logout();

    console.log('User logged out');
    this.router.navigate(['/']);
  }
  onUpdateDetails() {
    // Optionally navigate to an update page or open dialog
    this.router.navigate(['/privateMain/update-details']);
  }


    startCountdown() {
    this.timerInterval = setInterval(() => {
      
      const expiresAt = this.authService.getTokenExpiry();
      const remaining = expiresAt - Date.now();

      if (remaining <= 0) {
        clearInterval(this.timerInterval);
        this.remainingTime = 'Session expired';
        this.authService.logout(); // auto logout
        return;
      }

      this.remainingTime = this.formatTime(remaining);
    }, 1000);
  }

  formatTime(ms: number): string {
    const totalSec = Math.floor(ms / 1000);
    const min = Math.floor(totalSec / 60);
    const sec = totalSec % 60;
    return `${min}m ${sec < 10 ? '0' + sec : sec}s`;
  }

  ngOnDestroy() {
    clearInterval(this.timerInterval);
  }


}





