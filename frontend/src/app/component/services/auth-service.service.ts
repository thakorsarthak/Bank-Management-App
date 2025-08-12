import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthServiceService {
private loggedInSubject = new BehaviorSubject<boolean>(this.hasToken());
  public isLoggedIn$ = this.loggedInSubject.asObservable();

  constructor(private router : Router) {}

  private hasToken(): boolean {
    return !!localStorage.getItem('token');
  }

  login(token: string): void {
    localStorage.setItem('token', token);
    this.loggedInSubject.next(true);
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('expiresAt');
    this.loggedInSubject.next(false);
    this.router.navigate(['/login']);
  }

   getTokenExpiry(): number {
    return parseInt(localStorage.getItem('expiresAt') || '0', 10);
  }
  

  isLoggedIn(): boolean {
    return this.hasToken();
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }
  
  //  getAccountNumber(): string | null {
  //   const token = this.getToken();
  //   if (!token) return null;
  
  //   try {
  //     const decoded: any = jwtDecode(token);
  //     return decoded.accountNumber || null;
  //   } catch (error) {
  //     console.error('Error decoding token', error);
  //     return null;
  //   }
  // }
}


