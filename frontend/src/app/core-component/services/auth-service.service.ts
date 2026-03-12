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

  constructor(private router: Router) { }

  private hasToken(): boolean {
    return !!localStorage.getItem('token');
  }

  login(token: string, expiresAt: number): void {
    localStorage.setItem('token', token);
    console.log("Token stored:", token);
    this.loggedInSubject.next(true);
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('expiresAt');
    this.loggedInSubject.next(false);
    this.router.navigate(['/login']);
  }

  getTokenExpiry(): number {
    const payload = this.getPayload();
    if (!payload?.exp) return 0;

    // exp is in seconds → convert to milliseconds
    return payload.exp * 1000;
  }


  isLoggedIn(): boolean {
    return this.hasToken();
  }

  checkExpiry(): void {
    if (Date.now() > this.getTokenExpiry()) {
      this.logout();
    }
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  //decoding token

  private getPayload(): any | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      return jwtDecode<any>(token);
    } catch {
      return null;
    }
  }


  getRole(): any  {
    const payload = this.getPayload();
    if(!payload) return null;
    
    console.log('Decoded Payload:', payload);
  return payload.role ;
  }

  isAdmin(): boolean {
    return this.getRole() === 'ADMIN';
  }

  isEmployee(): boolean {
    return this.getRole() === 'EMPLOYEE';
  }

  isUser(): boolean {
    return this.getRole() === 'USER';
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


