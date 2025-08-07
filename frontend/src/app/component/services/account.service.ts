import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
constructor(private http: HttpClient) { }

  createAccount(obj:any):Observable<any[]>{
      return this.http.post<any>("http://localhost:6011/bankapp/main/create",obj)
  }
  loginAccount(obj:any):Observable<any>{
      return this.http.post<any>("http://localhost:6011/bankapp/main/login-account",obj)
  }

  sendOtp(obj:any):Observable<any[]>{
      return this.http.post<any>("http://localhost:6011/bankapp/otp/send",obj)
  }

  verifyOtp(obj:any):Observable<any[]>{
      return this.http.post<any>("http://localhost:6011/bankapp/otp/verify",obj)
  }
   setPinWithOtp(obj:any):Observable<any[]>{
      return this.http.put<any>("http://localhost:6011/bankapp/account/changePinWithOtp",obj)
  }
 
}
