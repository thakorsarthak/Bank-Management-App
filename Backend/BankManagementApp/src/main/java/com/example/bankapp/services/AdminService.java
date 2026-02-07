package com.example.bankapp.services;

import java.util.List;

import com.example.bankapp.DTO.AccountUpdateRequestDTO;
import com.example.bankapp.DTO.AdminDashboardResponseDTO;
import com.example.bankapp.DTO.AdminEmployeeResponseDTO;
import com.example.bankapp.DTO.AdminUserResponseDTO;
import com.example.bankapp.DTO.CreateStaffDTO;
import com.example.bankapp.DTO.EmployeeListDTO;
import com.example.bankapp.DTO.UpdateEmployeeRequestDTO;
import com.example.bankapp.entity.Employee;
import com.example.bankapp.enums.AccountStatus;
import com.example.bankapp.enums.Designation;

public interface AdminService {

	public void createEmployeeOrManager(CreateStaffDTO request);

	List<Employee> getAllStaff();

	AdminDashboardResponseDTO getStats();

	public void updateStatusEmployee(Long employeeId, AccountStatus accountStatus);

	public void updateDesignation(Long accountId, Designation designation);

	public AdminUserResponseDTO getUsers(int page, int size, Long branchId, String sortField, String sortOrder,
			AccountStatus status);

	public AdminEmployeeResponseDTO getEmployees(
            int page,
            int size,
            Long branchId,
            String sortField,
            String sortOrder,
            AccountStatus status,
            Designation designation);

	//public List<AdminEmployeeResponseDTO> getAllEmployees();

	public void updateEmployee(Long employeeID , AccountUpdateRequestDTO employee);
	
	public void updateUser(Long userId , AccountUpdateRequestDTO user);

	List<EmployeeListDTO> saveAllEmployees(List<CreateStaffDTO> staffList);


}
