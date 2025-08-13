import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GlobalAPIResponse } from '../Models/global-api-response';
import { Transaction } from '../Models/Transaction';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  
constructor(private http: HttpClient) {}

  transferMoney(payload: any) {
    return this.http.put( "http://localhost:6011/bankapp/transaction/tranfer", payload);
  }
   getAccountHolderName(accountNumber: string): Observable<any> {
    return this.http.get<any>(`http://localhost:6011/bankapp/account/accountHolderName/${accountNumber}`);
  }
 
  // getTransactionHistory(accountNumber: string): Observable<any> {
  //   return this.http.get<any>(`http://localhost:6011/bankapp/transaction/history/${accountNumber}`);
  // }

  getTransactionHistory( ): Observable<GlobalAPIResponse<Transaction[]>> {
    return this.http.get<GlobalAPIResponse<Transaction[]>>(`http://localhost:6011/bankapp/transaction/history`);
  }

  getAccountHolderDetails(): Observable<any> {
    return this.http.get<any>(`http://localhost:6011/bankapp/account/accountHolderDetail`);
  }
}
