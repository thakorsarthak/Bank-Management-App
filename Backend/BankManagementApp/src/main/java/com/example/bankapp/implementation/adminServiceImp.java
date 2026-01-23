package com.example.bankapp.implementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bankapp.DTO.AdminDashboardResponseDTO;
import com.example.bankapp.DTO.AdminEmployeeResponseDTO;
import com.example.bankapp.DTO.CreateStaffDTO;
import com.example.bankapp.DTO.EmployeeListDTO;
import com.example.bankapp.DTO.UpdateEmployeeRequestDTO;
import com.example.bankapp.Exception.CustomValidationException;
import com.example.bankapp.Exception.FieldError;
import com.example.bankapp.Specification.EmployeeSpecification;
import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.Branch;
import com.example.bankapp.entity.Employee;
import com.example.bankapp.enums.AccountStatus;
import com.example.bankapp.enums.Designation;
import com.example.bankapp.enums.Role;
import com.example.bankapp.repository.AccountRepo;
import com.example.bankapp.repository.AuditRepo;
import com.example.bankapp.repository.BranchRepository;
import com.example.bankapp.repository.EmployeeRepo;
import com.example.bankapp.services.AdminService;
import com.example.bankapp.services.AuditService;
import com.example.bankapp.util.EmployeeSortBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class adminServiceImp implements AdminService {

	private final AccountRepo accountRepo;

	private final EmployeeRepo employeeRepo;

	private final AuditRepo auditRepo;

	private final AuditService auditService;

	private final PasswordEncoder passwordEncoder;
	
	private final BranchRepository  branchRepo;
	
	@Override
	public  AdminDashboardResponseDTO getStats() {
		
		long totalEmp = employeeRepo.count();
		long activeEmp = employeeRepo.countByAccountStatus(AccountStatus.ACTIVE);
		long inactiveEmp = employeeRepo.countByAccountStatus(AccountStatus.INACTIVE);
		
		long totalUser = accountRepo.count();
		long activeUser = accountRepo.countByStatus(AccountStatus.ACTIVE);
		long inactiveUser = accountRepo.countByStatus(AccountStatus.INACTIVE);
		
		
		return new 	AdminDashboardResponseDTO(
				new AdminDashboardResponseDTO.Stats(totalEmp, activeEmp, inactiveEmp),
                new AdminDashboardResponseDTO.Stats(totalUser, activeUser, inactiveUser));
		
	}

	@Override
	public void createEmployeeOrManager(CreateStaffDTO request) {

		List<FieldError> errors = new ArrayList<>();

		if (request.getRole() == Role.ADMIN || request.getRole() == Role.USER) {

			errors.add(new FieldError("Invalid Request", "Can't create User or Admin"));
			// throw new IllegalArgumentException("Invalid role for creation");
		}

		if (accountRepo.existsByEmail(request.getEmail())) {
			errors.add(new FieldError("email", "Employee with this email already exist"));
		}
		
		
		Branch branch = branchRepo
		        .findByBranchCodeAndActiveTrue(request.getBranchCode())
		        .orElseThrow(() -> new CustomValidationException(
		            "Invalid Branch",
		            List.of(new FieldError("branchCode", "Branch not found or inactive"))
		        ));

		Account account = new Account();

		account.setEmail(request.getEmail());
		account.setPassword(passwordEncoder.encode(request.getPassword()));
		account.setBranch(branch);
		account.setRole(request.getRole());
		account.setStatus(AccountStatus.ACTIVE);

		accountRepo.save(account);

		Employee profile = new Employee();
		profile.setAccount(account);
		profile.setFullName(request.getFullName());
		profile.setBranchCode(request.getBranchCode());
		profile.setJoiningDate(LocalDate.now());
		profile.setDesignation(request.getDesignation());


		employeeRepo.save(profile);
	}

	@Override
	public List<Employee> getAllStaff() {
		return employeeRepo.findAll();
	}

	@Override
	@Transactional
	public void updateStatusEmployee(Long employeeId, AccountStatus accountStatus) {

		Optional<Employee> emp = employeeRepo.findById(employeeId);

		Employee employee = emp.get();

		// Account account = accountRepo.findById(id);
		if (emp.isEmpty()) {

			throw new RuntimeException("Account not found");
		}

		Account acc = employee.getAccount();

		acc.setStatus(accountStatus);
	}



	@Override
	@Transactional
	public void updateDesignation(Long accountId, Designation designation) {

		System.out.println("Inside service to update designation");
		Optional<Employee> employee = employeeRepo.findByAccountId(accountId);
		if (employee.isEmpty()) {

			throw new RuntimeException("Account not found");

		}

		Employee e = employee.get();
		System.out.println("Designation changed to " + designation);
		e.setDesignation(designation);
	}


	@Override
	public AdminEmployeeResponseDTO getEmployees(
	        int page,
	        int size,
	        String sortField,
	        String sortOrder,
	        AccountStatus status,
	        Designation designation
	) {

		Pageable pageable = PageRequest.of(page, size , EmployeeSortBuilder.build(sortField, sortOrder));

		Specification<Employee> specification = Specification.where(null);

		specification = specification.and(EmployeeSpecification.hasAccountStatus(status));

		specification = specification.and(
	            EmployeeSpecification.hasDesignation(designation)
	    );

		Page<Employee> empPage = employeeRepo.findAll(specification,pageable);

		return  AdminEmployeeResponseDTO.fromPage(empPage);
	}



//	public List<AdminEmployeeResponseDTO> getAllEmployees() {
//
//		return employeeRepo.findAll().stream()
//				.map(emp -> new AdminEmployeeResponseDTO(emp.getEmployeeId(), emp.getFullName(),
//						emp.getAccount().getEmail(), emp.getBranchCode(), emp.getDesignation(),
//						emp.getAccount().getStatus(), emp.getJoiningDate()))
//				.toList();
//	}

	@Override
	@Transactional
	public void updateEmployee(Long employeeId, UpdateEmployeeRequestDTO req) {

		Optional<Employee> employee = employeeRepo.findByEmployeeId(employeeId);

		Employee emp = employee.get();

		System.out.println(emp);

		Account account = emp.getAccount();

		System.out.println(account);

		if (req.getFullName() != null) {
			emp.setFullName(req.getFullName());
		}

		if (req.getBranchCode() != null) {
			emp.setBranchCode(req.getBranchCode());
		}

		if (req.getDesignation() != null) {
			emp.setDesignation(req.getDesignation());
		}

		if (req.getStatus() != null) {
			account.setStatus(req.getStatus());
		}
	}

}
