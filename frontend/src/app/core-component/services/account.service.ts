import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';


@Injectable({
  providedIn: 'root'
})
export class AccountService {

private apiUrl = environment.apiUrl;   
    
constructor(private http: HttpClient) { }

  createAccount(obj:any):Observable<any[]>{
      return this.http.post<any>(`${this.apiUrl}/main/create`,obj)
  }
  loginAccount(obj:any ,  { headers }: { headers: HttpHeaders }):Observable<any>{
      return this.http.post<any>(`${this.apiUrl}/main/login-account`,obj, { headers })
  }

  refreshToken(refreshToken: string) {

  return this.http.post<any>(
    `${this.apiUrl}/main/refresh`,
    { refreshToken }
  );
}

  sendOtp(obj:any):Observable<any[]>{
      return this.http.post<any>(`${this.apiUrl}/otp/send`,obj)
  }

  verifyOtp(obj:any):Observable<any[]>{
      return this.http.post<any>(`${this.apiUrl}/otp/verify`,obj)
  }
   setPinWithOtp(obj:any):Observable<any[]>{
      return this.http.put<any>(`${this.apiUrl}/account/changePasswordWithOtp`,obj)
  }

  logoutAccount():Observable<any>{
      return this.http.post<any>(`${this.apiUrl}/account/logout`,{})
  }
 
}
