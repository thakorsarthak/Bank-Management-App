import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http: HttpClient) { }

  getEmployees(params: any) {
    return this.http.get<any>('http://localhost:6011/bankapp/admin/employee/getAllStaff', { params });
  }

  updateStatus(accountId: number, active: boolean) {
    return this.http.put(`/api/admin/staff/${accountId}/status`, { active });
  }

  updateEmployee(accountId: number, payload: any) {
  return this.http.put(
    `http://localhost:6011/bankapp/admin/employee/${accountId}`,
    payload
  );
}


}
