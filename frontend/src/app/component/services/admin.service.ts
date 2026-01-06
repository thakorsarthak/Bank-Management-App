import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GlobalAPIResponse } from '../Models/global-api-response';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http: HttpClient) { }

  getEmployees(params: any) {
    return this.http.get<any>('http://localhost:6011/bankapp/admin/employee/getAllStaff', { params });
  }
  

  updateEmployeeStatus(employeeId: number, payload: { status: string }) {
    return this.http.patch<GlobalAPIResponse<any>>(
      `http://localhost:6011/bankapp/admin/employee/${employeeId}/updateStatus`, payload);
  }

  updateEmployee(employeeId: number, payload: any) {
    return this.http.patch<GlobalAPIResponse<any>>(
      `http://localhost:6011/bankapp/admin/employee/${employeeId}/updateAllDetails`,
      payload
    );
  }


}
