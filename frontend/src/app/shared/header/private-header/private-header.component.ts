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

import { AvatarModule } from 'primeng/avatar';
import { OverlayBadgeModule } from 'primeng/overlaybadge';
import { AuthServiceService } from '../../../core-component/services/auth-service.service';
import { AccountService } from '../../../core-component/services/account.service';

@Component({
  selector: 'app-private-header',
  imports: [CommonModule, RouterLink, AvatarModule, OverlayBadgeModule,
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

  constructor(private authService: AuthServiceService, private router: Router, private accountService: AccountService) { }



  ngOnInit() {

    this.startCountdown();
    const expiresAt = this.authService.getTokenExpiry();
    console.log('Expires at:', new Date(expiresAt));
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
    this.accountService.logoutAccount().subscribe({
      next: () => {
        console.log('Logout successful');
      },
      error: (err) => {
        console.error('Logout failed', err);
      }
    });

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
  // Clear any existing interval before starting a new one
  if (this.timerInterval) {
    clearInterval(this.timerInterval);
  }

  this.timerInterval = setInterval(() => {
    const expiresAt = this.authService.getTokenExpiry(); // must return number (ms)
    const remaining = expiresAt - Date.now();

    if (remaining <= 0) {
      clearInterval(this.timerInterval);
      this.timerInterval = null; // prevent stale reference

      const role = localStorage.getItem('role');

      const refreshToken = localStorage.getItem('refreshToken');

  // ADD THIS
  console.log('[Timer expired] role:', role);
  console.log('[Timer expired] refreshToken:', refreshToken);
  console.log('[Timer expired] full localStorage:', {...localStorage});

      if (role === 'USER') {
        this.authService.logout();
        this.router.navigate(['/']);
        return;
      }


      

      if (!refreshToken) {
        this.authService.logout();
        this.router.navigate(['/']);
        return;
      }

      this.accountService.refreshToken(refreshToken).subscribe({
        next: (response) => {
          // Store values before restarting
          localStorage.setItem('token', response.accessToken);
          localStorage.setItem('refreshToken', response.refreshToken);
          localStorage.setItem('expiresAt', String(response.expiresAt)); // ensure string

          this.startCountdown(); // restart AFTER storing
        },
        error: () => {
          this.authService.logout();
          this.router.navigate(['/']);
        }
      });
    } else {
      this.remainingTime = this.formatTime(remaining);
    }

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