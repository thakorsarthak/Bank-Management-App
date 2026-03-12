export interface Employee {
  employeeId: number;
  fullName: string;
  email: string;
  branchCode: string;
  designation: 'CASHIER' | 'BRANCH_MANAGER' | 'LOAN_OFFICER' | 'SUPPORT';
  status: 'ACTIVE' | 'INACTIVE' | 'SUSPENDED' ;
  joiningDate: string; // ISO date string
}
