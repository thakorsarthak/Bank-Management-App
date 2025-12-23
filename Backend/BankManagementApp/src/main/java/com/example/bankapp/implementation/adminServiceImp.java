package com.example.bankapp.implementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.bankapp.DTO.CreateStaffDTO;
import com.example.bankapp.Exception.FieldError;
import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.Employee;
import com.example.bankapp.enums.AccountStatus;
import com.example.bankapp.enums.Role;
import com.example.bankapp.repository.AccountRepo;
import com.example.bankapp.repository.EmployeeRepo;
import com.example.bankapp.services.AdminService;


import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class adminServiceImp implements AdminService {

	private final AccountRepo accountRepo;
	
	private final EmployeeRepo employeeRepo;
	
	private final PasswordEncoder passwordEncoder;

	@Override
	public void createEmployeeOrManager(CreateStaffDTO request) {
		
		List<FieldError> errors = new ArrayList<>();
		
		if (request.getRole() == Role.ADMIN || request.getRole() == Role.USER) {
			
			errors.add(new FieldError("Invalid Request", "Can't create User or Admin"));
			//throw new IllegalArgumentException("Invalid role for creation");
		}
		
		if(accountRepo.existsByEmail(request.getEmail())) {
			errors.add(new FieldError("email", "Employee with this email already exist"));
		}
		
		Account account = new Account();
		
		account.setEmail(request.getEmail());
		account.setPassword(passwordEncoder.encode(request.getPassword()));
		account.setBranchCode(request.getBranchCode());
		account.setRole(request.getRole());
		account.setStatus(AccountStatus.ACTIVE);
		
		accountRepo.save(account);
		
		Employee profile = new Employee();
		profile.setAccount(account);
		profile.setFullName(request.getFullName());
		profile.setBranchCode(request.getBranchCode());
		profile.setDesignation(request.getDesignation());
		profile.setJoiningDate(LocalDate.now());

		employeeRepo.save(profile);
	}
	
	
	@Override 
	 public List<Employee> getAllStaff() {
        return employeeRepo.findAll();
    }
	
	
}
