import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CalendarModule } from 'primeng/calendar';
import { CardModule } from 'primeng/card';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { PaginatorModule } from 'primeng/paginator';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { ToastModule } from 'primeng/toast';

import { MessageService } from 'primeng/api';

import { Dialog } from "primeng/dialog";
import { Employee } from '../../../core-component/models/employee.model';
import { AdminService } from '../../../core-component/services/admin.service';

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

  stats: any;
  dashboardData: any;
  employees: Employee[] = [];
  users: any[] = [];
  totalEmployees = 0;
  activeEmployee: any;
  inActiveEmployee: any;
  totalUsers = 0;
  activeUsers: any;
  inActiveUsers: any;
  dashboardEmployeeStats: any;
  totalRecords = 0;
  loading = false;
  router: Router = new Router();

  // FILTER MODEL
  selectedEntity!: string;

  //for Card dropdown
  selectedtype!: string;

  

  searchName: string = '';
  status: string | null = null;
  designation: string | null = null;
  dateSort: string | null = null;
  branch: string | null = null;
  selectedBranch: number | null = null;
  branchSortOrder: 'ASC' | 'DESC' | null = null;
  selectedBranchId: number | null = null;

  dashboardOptions = [
    { label: 'Employee', value: 'EMPLOYEE' },
    { label: 'User', value: 'USER' },
    { label: 'Transaction', value: 'TRANSACTION' }
  ];

  selectedDashboard: string = 'EMPLOYEE';

  showEntityDialog = false;
  selectedEmployee!: Employee;

  selectedUser!: any;
  selectedUserId!: number;

  entityForm!: FormGroup;

  constructor(
    private adminService: AdminService,
    private fb: FormBuilder,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {

    this.adminService.getDashboardCard().subscribe(res => {
      this.dashboardData = res;
    });

    this.adminService.getStats().subscribe({
      next: (res) => {
        this.totalEmployees = res.data.employees.total;
        this.activeEmployee = res.data.employees.active;
        this.inActiveEmployee = res.data.employees.inactive;

        this.totalUsers = res.data.users.total;
        this.activeUsers = res.data.users.active;
        this.inActiveUsers = res.data.users.inactive;

      }

    });


    this.entityForm = this.fb.group({
      fullName: ['', Validators.required],
      branchCode: ['', Validators.required],
      designation: ['', Validators.required],
      status: ['', Validators.required],
      branchName: ['']
    });
  }

  //  OPTIONS for filter and dropdown //

  designationOptions = [
    { label: 'Cashier', value: 'CASHIER' },
    { label: 'Branch Manager', value: 'BRANCH_MANAGER' },
    { label: 'Loan Officer', value: 'LOAN_OFFICER' },
    { label: 'Support', value: 'SUPPORT' }
  ];

  branchOptions = [
    { label: 'All Branches', value: null },
    { label: 'Ahmedabad', value: 1 },
    { label: 'Surat', value: 2 },
    { label: 'Mumbai', value: 3 },
    { label: 'Bangalore', value: 4 }
  ];


  statusOptions = [
    { label: 'Active', value: 'ACTIVE' },
    { label: 'Inactive', value: 'INACTIVE' },
    { label: 'Suspended', value: 'SUSPENDED' },
    { label: 'Pending KYC', value: 'PENDING_KYC' }
  ];

  dateSortOptions = [
    { label: 'Newest First', value: 'DESC' },
    { label: 'Oldest First', value: 'ASC' }
  ];

  entityOptions = [
    { label: 'Employee', value: 'EMPLOYEE' },
    { label: 'User', value: 'USER' }
  ];


 onDashboardChange() {
  this.clearFilters();

  // Optional: change entity automatically
  if (this.selectedDashboard === 'EMPLOYEE') {
    this.selectedtype = 'EMPLOYEE';
  } else if (this.selectedDashboard === 'USER') {
    this.selectedtype = 'USER';
  }
}

  //  ENTITY CHANGE or choosing Employee/User //

  onEntityChange() {
    this.loadEntities(null);
    this.clearFilters();
    // if (this.selectedEntity === 'EMPLOYEE') {
    //   this.loadEmployees();
    // }
  }

  loadEntities(event: any) {
    console.log('Selected Entity:', this.selectedEntity, event);
    if (this.selectedEntity === 'EMPLOYEE') {
      this.loadEmployees(event);
    } else {
      this.loadUsers(event);
    }
  }


  // Implement user loading logic here 
  loadUsers(event?: any) {
    console.log('Loading users with event:', event);

    const first = event?.first ?? 0;
    const rows = event?.rows ?? 10;

    const page = Math.floor(first / rows);
    const size = rows;

    const sortField = event?.sortField ?? 'createdAt';
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

    this.adminService.getAllUser(params).subscribe({
      next: (res) => {
        this.users = res.data.users;
        this.totalRecords = res.data.totalRecords;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });

  }
  // main PAGINATION loading employee 

  loadEmployees(event?: any) {

    //only if employee entity is selected by admin

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
    if (this.selectedBranchId != null) {
      params.branchId = this.selectedBranchId;
    }

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
  applyUserFilters() {

    const params: any = {
      page: 0,
      size: 10
    };

    if (this.status)
      params.status = this.status;

    if (this.selectedBranchId != null) {
      params.branchId = this.selectedBranchId;
    }

    if (this.branch)
      // params.sortField = 'branch';
      params.branchId = this.branch;

    if (this.dateSort) {
      params.sortField = 'joiningDate';
      params.sortOrder = this.dateSort;
    }

    // if (this.branch)
    //   params.branch = this.branch;

    this.adminService.getAllUser(params)
      .subscribe(res => {
        this.users = res.data.users;
        this.totalRecords = res.data.totalRecords;
      });
  }
  applyEmployeeFilters() {

    const params: any = {
      page: 0,
      size: 10
    };

    if (this.designation)
      params.designation = this.designation;

    if (this.status)
      params.status = this.status;

    if (this.selectedBranchId != null)
      params.branchId = this.selectedBranchId;


    if (this.branch)
      // params.sortField = 'branch';
      params.branchId = this.branch;

    if (this.searchName)
      params.search = this.searchName;

    if (this.dateSort) {
      params.sortField = 'joiningDate';
      params.sortOrder = this.dateSort;
    }

    // if (this.branch)
    //   params.branch = this.branch;

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
    // this.branch = null;
    // this.refeshEmployees();
    this.loadEntities({ first: 0, rows: 10 });
    this.selectedBranchId = null;
    this.selectedBranch = null;
    this.branch = null;
  }

  // for Refreshing employees table  //

  // refeshEmployees() {
  //   this.loadEmployees({ first: 0, rows: 10 });
  // }


  //viewing USER details
  viewUser(user: any): void {
    this.selectedUser = user;

    this.selectedUserId = user.id;

    this.entityForm.patchValue({
      status: this.selectedUser.status,
      branchName: this.selectedUser.branch,
    });
    this.showEntityDialog = true;
  }

  viewMoreUser() {
    this.router.navigate(['/privateMain/adminUserDetails', this.selectedUser.id]);
  }

  //  Viewing employee 

  viewEmployee(employee: Employee): void {

    this.selectedEmployee = employee;

    this.entityForm.patchValue({
      fullName: employee.fullName,
      branchCode: employee.branchCode,
      designation: employee.designation,
      status: employee.status
    });

    this.showEntityDialog = true;
  }

  // for  UPDATE  //
  updateUser(): void {

    if (!this.selectedUser?.id) {

      console.log('Selected User:', this.selectedUser);
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'User id missing!'
      });
      return;
    }

    // if (this.entityForm.invalid) return;

    const payload = {
      status: this.entityForm.value.status,
    };

    this.adminService
      .updateUser(this.selectedUser.id, payload)
      .subscribe({
        next: (res) => {
          this.showEntityDialog = false;
          this.loadUsers();

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

  updateEmployee(): void {

    if (!this.selectedEmployee?.employeeId) {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Employee id missing!'
      });
      return;
    }

    if (this.entityForm.invalid) return;

    const payload = this.entityForm.value;

    this.adminService
      .updateEmployee(this.selectedEmployee.employeeId, payload)
      .subscribe({
        next: (res) => {
          this.showEntityDialog = false;
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
    this.showEntityDialog = false;
    this.entityForm.reset();
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

