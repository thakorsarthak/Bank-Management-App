package com.example.bankapp.services;

import java.util.List;

import com.example.bankapp.DTO.CreateStaffDTO;
import com.example.bankapp.entity.Employee;

public interface AdminService {

	public void createEmployeeOrManager(CreateStaffDTO request);

	List<Employee> getAllStaff();
}
