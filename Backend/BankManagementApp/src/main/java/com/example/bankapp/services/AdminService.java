package com.example.bankapp.services;

import java.util.List;


import org.springframework.http.ResponseEntity;

import com.example.bankapp.DTO.AdminEmployeeResponseDTO;
import com.example.bankapp.DTO.CreateStaffDTO;
import com.example.bankapp.DTO.UpdateEmployeeRequestDTO;
import com.example.bankapp.entity.Employee;
import com.example.bankapp.enums.AccountStatus;
import com.example.bankapp.enums.Designation;

public interface AdminService {

	public void createEmployeeOrManager(CreateStaffDTO request);

	List<Employee> getAllStaff();
	
	public void updateStatus(Long accountId , AccountStatus accountStatus) ;

	public void updateDesignation(Long accountId, Designation designation);
	
	public List<AdminEmployeeResponseDTO> getAllEmployees();
	 
	public void updateEmployee(Long employeeID , UpdateEmployeeRequestDTO employee);
}
