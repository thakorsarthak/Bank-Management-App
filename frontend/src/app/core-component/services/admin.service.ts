import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GlobalAPIResponse } from '../models/global-api-response.model';
import { environment } from '../../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http: HttpClient) { }

  private apiUrl = environment.apiUrl;

  getEmployees(params: any) {
    return this.http.get<any>(`${this.apiUrl}/admin/employee/getAllEmployee`, { params });
  }
  
  getAllUser(params: any) {
    return this.http.get<any>(`${this.apiUrl}/admin/user/getAllUser`, { params });
  }

  updateEmployeeStatus(employeeId: number, payload: { status: string }) {
    return this.http.patch<GlobalAPIResponse<any>>(
      `${this.apiUrl}/admin/employee/${employeeId}/updateStatus`, payload);
  }

  updateEmployee(employeeId: number, payload: any) {
    return this.http.patch<GlobalAPIResponse<any>>(
      `${this.apiUrl}/admin/employee/${employeeId}/updateAllDetails`,
      payload
    );
  }

   updateUser(accountId: number, payload: any) {
    return this.http.patch<GlobalAPIResponse<any>>(
      `${this.apiUrl}/admin/user/${accountId}/updateUser`,
      payload
    );
  }


}
