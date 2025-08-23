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

  getTransactionExcelHistoryByDate(fromDate: string, toDate: string ) {
  return this.http.get(`http://localhost:6011/bankapp/transaction/downloadTransactionHistory?fromDate=${fromDate}&toDate=${toDate}`, {
    responseType: 'blob' // Important for file download
  });
}

  getPaginatedHistory(page: number, size: number , sortByTime: string, sortByDirection: string) {
  return this.http.get<any>(`http://localhost:6011/bankapp/transaction/transactionHistory`, {
    params: { page, size , sortByTime,sortByDirection}
  });
}

  getAccountHolderDetails(): Observable<any> {
    return this.http.get<any>(`http://localhost:6011/bankapp/account/accountHolderDetail`);
  }
}
