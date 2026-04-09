import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environment/environment';


@Injectable({
  providedIn: 'root'
})
export class AccountService {

private apiUrl = environment.apiUrl;   
    
constructor(private http: HttpClient) { }

  createAccount(obj:any):Observable<any[]>{
      return this.http.post<any>(`${this.apiUrl}/bankapp/main/create`,obj)
  }
  loginAccount(obj:any):Observable<any>{
      return this.http.post<any>(`${this.apiUrl}/bankapp/main/login-account`,obj)
  }

  sendOtp(obj:any):Observable<any[]>{
      return this.http.post<any>(`${this.apiUrl}/bankapp/otp/send`,obj)
  }

  verifyOtp(obj:any):Observable<any[]>{
      return this.http.post<any>(`${this.apiUrl}/bankapp/otp/verify`,obj)
  }
   setPinWithOtp(obj:any):Observable<any[]>{
      return this.http.put<any>(`${this.apiUrl}/bankapp/account/changePasswordWithOtp`,obj)
  }
 
}
