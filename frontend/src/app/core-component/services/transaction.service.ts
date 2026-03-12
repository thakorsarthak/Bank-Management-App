import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GlobalAPIResponse } from '../models/global-api-response.model';
import { Transaction } from '../models/transaction.model';


@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  
constructor(private http: HttpClient) {}

  transferMoney(payload: any) {
    return this.http.put( "http://localhost:60000/bankapp/transaction/tranfer", payload);
  }
   getAccountHolderName(accountNumber: string): Observable<any> {
    return this.http.get<any>(`http://localhost:60000/bankapp/account/accountHolderName/${accountNumber}`);
  }
 
  // getTransactionHistory(accountNumber: string): Observable<any> {
  //   return this.http.get<any>(`http://localhost:60000/bankapp/transaction/history/${accountNumber}`);
  // }

  getTransactionHistory( ): Observable<GlobalAPIResponse<Transaction[]>> {
    return this.http.get<GlobalAPIResponse<Transaction[]>>(`http://localhost:60000/bankapp/transaction/history`);
  }

  getTransactionExcelHistoryByDate(fromDate: string, toDate: string ) {
  return this.http.get(`http://localhost:60000/bankapp/transaction/downloadTransactionHistory?fromDate=${fromDate}&toDate=${toDate}`, {
    responseType: 'blob' // Important for file download
  });
}

  getPaginatedHistory(page: number, size: number , sortByTime: string, sortByDirection: string) {
  return this.http.get<any>(`http://localhost:60000/bankapp/transaction/transactionHistory`, {
    params: { page, size , sortByTime,sortByDirection}
  });
}

  getAccountHolderDetails(): Observable<any> {
    return this.http.get<any>(`http://localhost:60000/bankapp/account/accountHolderDetail`);
  }
}
