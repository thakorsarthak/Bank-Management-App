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
import { LazyLoadEvent } from 'primeng/api';
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
  styleUrl: './admin-component.component.css'
})
export class AdminComponentComponent implements OnInit {

  employees: Employee[] = [];
  totalRecords = 0;
  loading = false;
  search = '';
  status: string | null = null;
  designation: string | null = null;
  showEmployeeDialog = false;
  selectedEmployee!: Employee;



  constructor(private adminService: AdminService,
    private fb: FormBuilder,) { }

  employeeForm!: FormGroup;


  ngOnInit(): void {
    this.employeeForm = this.fb.group({
      fullName: ['', Validators.required],
      branchCode: ['', Validators.required],
      designation: ['', Validators.required],
      status: ['', Validators.required]
    });

  }



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

  updateEmployee(): void {
    if (this.employeeForm.invalid) return;

    const payload = this.employeeForm.value;

    this.adminService
      .updateEmployee(this.selectedEmployee.accountId, payload)
      .subscribe(() => {
        this.showEmployeeDialog = false;
        this.loadEmployees();
      });
  }

  closeDialog(): void {
    this.showEmployeeDialog = false;
    this.employeeForm.reset();
  }


  loadEmployees(event?: TableLazyLoadEvent): void {
    this.loading = true;

    const params = {
      search: this.search,
      status: this.status,
      designation: this.designation
    };



    this.adminService.getEmployees(params).subscribe({
      next: (res) => {
        this.employees = res.data;
        this.totalRecords = res.totalRecords;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  //  Filter trigger
  applyFilters(): void {
    this.loadEmployees({
      first: 0,
      rows: 10
    });
  }

  //  Clear filters
  clearFilters(): void {
    this.search = '';
    this.status = null;
    this.designation = null;
    this.applyFilters();
  }

  // //  View employee
  // viewEmployee(accountId: number): void {
  //   console.log('View employee:', accountId);
  //   // navigate later
  // }

  // 🏷 Change designation
  changeDesignation(employee: any): void {
    console.log('Change designation:', employee);
    // open dialog later
  }

  //  Enable / Disable employee
  updateStatus(accountId: number, active: boolean): void {
    this.adminService.updateStatus(accountId, active).subscribe({
      next: () => this.loadEmployees(),
      error: (err) => console.error(err)
    });
  }

}
