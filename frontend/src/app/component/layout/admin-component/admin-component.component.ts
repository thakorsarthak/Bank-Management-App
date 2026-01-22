import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink, RouterOutlet } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CalendarModule } from 'primeng/calendar';
import { CardModule } from 'primeng/card';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { PaginatorModule } from 'primeng/paginator';
import { TableLazyLoadEvent, TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { ToastModule } from 'primeng/toast';
import { Employee } from '../../Models/employee';
import { LazyLoadEvent, MessageService } from 'primeng/api';
import { AdminService } from '../../services/admin.service';
import { Dialog } from "primeng/dialog";

@Component({
  selector: 'app-admin-component',
  imports: [RouterLink, RouterOutlet,
    CommonModule,
    FormsModule, ReactiveFormsModule,
    ConfirmPopupModule, ToastModule, ConfirmDialogModule,
    CardModule,
    TableModule,
    TagModule,
    CalendarModule,
    ButtonModule,
    InputTextModule,
    DropdownModule,
    PaginatorModule, Dialog],
  templateUrl: './admin-component.component.html',
  styleUrl: './admin-component.component.css',
  providers: [MessageService]
})
export class AdminComponentComponent implements OnInit {

  employees: Employee[] = [];
  totalRecords = 0;
  loading = false;

  // FILTER MODEL
  selectedEntity!: string;

  searchName: string = '';
  status: string | null = null;
  designation: string | null = null;
  dateSort: string | null = null;
  branch: string | null = null;

  showEmployeeDialog = false;
  selectedEmployee!: Employee;

  employeeForm!: FormGroup;

  constructor(
    private adminService: AdminService,
    private fb: FormBuilder,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {

    this.employeeForm = this.fb.group({
      fullName: ['', Validators.required],
      branchCode: ['', Validators.required],
      designation: ['', Validators.required],
      status: ['', Validators.required]
    });
  }

  //  OPTIONS for filter and dropdown //

  designationOptions = [
    { label: 'Cashier', value: 'CASHIER' },
    { label: 'Branch Manager', value: 'BRANCH_MANAGER' },
    { label: 'Loan Officer', value: 'LOAN_OFFICER' },
    { label: 'Support', value: 'SUPPORT' }
  ];

  statusOptions = [
    { label: 'Active', value: 'ACTIVE' },
    { label: 'Inactive', value: 'INACTIVE' }
  ];

  dateSortOptions = [
    { label: 'Newest First', value: 'DESC' },
    { label: 'Oldest First', value: 'ASC' }
  ];

  entityOptions = [
    { label: 'Employee', value: 'EMPLOYEE' },
    { label: 'User', value: 'USER' }
  ];

  branchOptions: any[] = [];

  //  ENTITY CHANGE or choosing Employee/User //

  onEntityChange() {
    this.clearFilters();

    // if (this.selectedEntity === 'EMPLOYEE') {
    //   this.loadEmployees();
    // }
  }

  // main PAGINATION loading employee //

  loadEmployees(event?: any) {

    //only if employee entity is selected by admin
     if (this.selectedEntity !== 'EMPLOYEE') {
    return;
  }

    this.loading = true;

    const first = event?.first ?? 0;
    const rows = event?.rows ?? 10;

    const page = Math.floor(first / rows);
    const size = rows;

    const sortField = event?.sortField ?? 'joiningDate';
    const sortOrder =
      event?.sortOrder !== undefined
        ? (event.sortOrder === 1 ? 'ASC' : 'DESC')
        : 'ASC';

    const params: any = {
      page,
      size,
      sortField,
      sortOrder
    };

    if (this.status) params.status = this.status;

    this.adminService.getEmployees(params).subscribe({
      next: (res) => {
        this.employees = res.data.employees;
        this.totalRecords = res.data.totalRecords;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  //  filtering block or logics //

  applyEmployeeFilters() {

    const params: any = {
      page: 0,
      size: 10
    };

    if (this.designation)
      params.designation = this.designation;

    if (this.status)
      params.status = this.status;

    if (this.searchName)
      params.search = this.searchName;

    if (this.dateSort) {
      params.sortField = 'joiningDate';
      params.sortOrder = this.dateSort;
    }

    if (this.branch)
      params.branch = this.branch;

    this.adminService.getEmployees(params)
      .subscribe(res => {
        this.employees = res.data.employees;
        this.totalRecords = res.data.totalRecords;
      });
  }

  // filter CLEAR and refreshing employee table //

  clearFilters() {
    this.searchName = '';
    this.status = null;
    this.designation = null;
    this.dateSort = null;
    this.branch = null;
    // this.refeshEmployees();
    this.loadEmployees({ first: 0, rows: 10 });
  }

  // for Refreshing employees table  //

  // refeshEmployees() {
  //   this.loadEmployees({ first: 0, rows: 10 });
  // }

  //  Viewing employee //

  viewEmployee(employee: Employee): void {

    this.selectedEmployee = employee;

    this.employeeForm.patchValue({
      fullName: employee.fullName,
      branchCode: employee.branchCode,
      designation: employee.designation,
      status: employee.status
    });

    this.showEmployeeDialog = true;
  }

  // for  UPDATE  //

  updateEmployee(): void {

    if (!this.selectedEmployee?.employeeId) {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Employee id missing!'
      });
      return;
    }

    if (this.employeeForm.invalid) return;

    const payload = this.employeeForm.value;

    this.adminService
      .updateEmployee(this.selectedEmployee.employeeId, payload)
      .subscribe({
        next: (res) => {
          this.showEmployeeDialog = false;
          this.loadEmployees();

          this.messageService.add({
            severity: res.success ? 'success' : 'warn',
            summary: res.success ? 'Success' : 'Warning',
            detail: res.message
          });
        },
        error: (err) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: err.error?.message || 'Something went wrong'
          });
        }
      });
  }

  closeDialog(): void {
    this.showEmployeeDialog = false;
    this.employeeForm.reset();
  }

  // for Updating  STATUS  //

  updateStatus(employeeId: number, status: 'ACTIVE' | 'INACTIVE') {

    this.adminService.updateEmployeeStatus(employeeId, { status })
      .subscribe({
        next: (res) => {
          this.messageService.add({
            severity: 'success',
            summary: 'Success',
            detail: res.message
          });
          this.loadEmployees();
        },
        error: (err) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: err.error?.message || 'Status update failed'
          });
        }
      });
  }
}

