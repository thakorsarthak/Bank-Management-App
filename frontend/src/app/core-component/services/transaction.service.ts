import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GlobalAPIResponse } from '../models/global-api-response.model';
import { Transaction } from '../models/transaction.model';
import { environment } from '../../../environments/environment';


@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  private apiUrl = environment.apiUrl;
  
constructor(private http: HttpClient) {}

  transferMoney(payload: any) {
    return this.http.put( `${this.apiUrl}/transaction/transfer`, payload);
  }
   getAccountHolderName(accountNumber: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/account/accountHolderName/${accountNumber}`);
  }
 
  // getTransactionHistory(accountNumber: string): Observable<any> {
  //   return this.http.get<any>(`${this.apiUrl}/transaction/history/${accountNumber}`);
  // }

  getTransactionHistory( ): Observable<GlobalAPIResponse<Transaction[]>> {
    return this.http.get<GlobalAPIResponse<Transaction[]>>(`${this.apiUrl}/transaction/history`);
  }

   getTransactionCardHistory( ): Observable<GlobalAPIResponse<Transaction[]>> {
    return this.http.get<GlobalAPIResponse<Transaction[]>>(`${this.apiUrl}/transaction/cardHistory`);
  }

   

  getTransactionExcelHistoryByDate(fromDate: string, toDate: string ) {
  return this.http.get(`${this.apiUrl}/transaction/downloadTransactionHistory?fromDate=${fromDate}&toDate=${toDate}`, {
    responseType: 'blob' // Important for file download
  });
}

  getPaginatedHistory(page: number, size: number , sortByTime: string, sortByDirection: string) {
  return this.http.get<any>(`${this.apiUrl}/transaction/transactionHistory`, {
    params: { page, size , sortByTime,sortByDirection}
  });
}

  getAccountHolderDetails(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/account/accountHolderDetail`);
  }
}
