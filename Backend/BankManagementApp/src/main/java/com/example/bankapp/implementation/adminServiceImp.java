package com.example.bankapp.implementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bankapp.DTO.AccountUpdateRequestDTO;
import com.example.bankapp.DTO.AdminDashboardResponseDTO;
import com.example.bankapp.DTO.AdminEmployeeResponseDTO;
import com.example.bankapp.DTO.AdminUserResponseDTO;
import com.example.bankapp.DTO.AdminUserTableResponseDTO;
import com.example.bankapp.DTO.CreateStaffDTO;
import com.example.bankapp.DTO.EmployeeListDTO;
import com.example.bankapp.DTO.NotificationRequestDTO;
import com.example.bankapp.Exception.CustomValidationException;
import com.example.bankapp.Exception.FieldError;
import com.example.bankapp.Specification.AccountSpecification;
import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.Branch;
import com.example.bankapp.entity.Employee;
import com.example.bankapp.enums.AccountStatus;
import com.example.bankapp.enums.AuditAction;
import com.example.bankapp.enums.Designation;
import com.example.bankapp.enums.Role;
import com.example.bankapp.repository.AccountRepo;
import com.example.bankapp.repository.AuditRepo;
import com.example.bankapp.repository.BranchRepository;
import com.example.bankapp.repository.EmployeeRepo;
import com.example.bankapp.services.AdminService;
import com.example.bankapp.services.AuditService;
import com.example.bankapp.services.JWTservices;
import com.example.bankapp.services.NotificationService;
import com.example.bankapp.util.AccountStatusEmailTemplate;
import com.example.bankapp.util.EmployeeSortBuilder;
import com.example.bankapp.util.UserSortBulder;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class adminServiceImp implements AdminService {

	private final AccountRepo accountRepo;

	private final EmployeeRepo employeeRepo;

	private final AuditRepo auditRepo;

	private final AuditService auditService;

	private final PasswordEncoder passwordEncoder;

	private final BranchRepository branchRepo;
	
	private final AuditService auditLogService;
	
	private final JWTservices jService;

	private final NotificationService notificationService;

	// response on dashboard
	@Override
	public AdminDashboardResponseDTO getStats() {

		long totalEmp = employeeRepo.count();
		long activeEmp = employeeRepo.countByAccountStatus(AccountStatus.ACTIVE);
		long inactiveEmp = employeeRepo.countByAccountStatus(AccountStatus.INACTIVE);

		long totalUser = accountRepo.count();
		long activeUser = accountRepo.countByStatus(AccountStatus.ACTIVE);
		long inactiveUser = accountRepo.countByStatus(AccountStatus.INACTIVE);

		return new AdminDashboardResponseDTO(new AdminDashboardResponseDTO.Stats(totalEmp, activeEmp, inactiveEmp),
				new AdminDashboardResponseDTO.Stats(totalUser, activeUser, inactiveUser));

	}

	// creating staff this method works due to  Persistence Context [hibernate session] only
	@Override
	@Transactional
	public void createEmployeeOrManager(CreateStaffDTO request) {

		List<FieldError> errors = new ArrayList<>();

		if (request.getRole() == Role.ADMIN || request.getRole() == Role.USER) {
			errors.add(new FieldError("Invalid Request", "Can't create User or Admin"));
			// throw new IllegalArgumentException("Invalid role for creation");
		}

		if (accountRepo.existsByEmail(request.getEmail())) {
			errors.add(new FieldError("email", "Employee with this email already exist"));
		}


	    if (!errors.isEmpty()) {
	        throw new CustomValidationException("Validation Failed", errors); //  STOP HERE
	    }

		Branch branch = branchRepo.findByBranchCodeAndActiveTrue(request.getBranchCode())
				.orElseThrow(() -> new CustomValidationException("Invalid Branch",
						List.of(new FieldError("branchCode", "Branch not found or inactive"))));

		Account account = new Account(); // transient not stored in db till now
		account.setAccountHolderName(request.getFullName());
		account.setEmail(request.getEmail());
		account.setPassword(passwordEncoder.encode(request.getPassword()));
		account.setBranch(branch);
		account.setRole(request.getRole());
		account.setStatus(AccountStatus.ACTIVE);

		accountRepo.save(account); // still not saved  its kind off scheduled to be inserted

		System.out.println(account);

		Employee profile = new Employee();
		profile.setAccount(account);
		profile.setFullName(request.getFullName());
		profile.setBranchCode(request.getBranchCode());
		profile.setJoiningDate(LocalDate.now());
		profile.setDesignation(request.getDesignation());

		employeeRepo.save(profile);
	}

	// for adding bulk staff

	@Transactional
	@Override
	public List<EmployeeListDTO> saveAllEmployees(List<CreateStaffDTO> staffList) {

	    List<Account> accounts = staffList.stream().map(dto -> {
	        Branch branch = branchRepo.findByBranchCodeAndActiveTrue(dto.getBranchCode())
	            .orElseThrow(() -> new CustomValidationException("Invalid Branch",
	                List.of(new FieldError("branchCode", "Branch not found or inactive"))));

	        Account account = new Account();
	        account.setAccountHolderName(dto.getFullName());
	        account.setEmail(dto.getEmail());
	        account.setPassword(passwordEncoder.encode(dto.getPassword()));
	        account.setBranch(branch);
	        account.setRole(dto.getRole());
	        account.setStatus(AccountStatus.ACTIVE);
	        return account;
	    }).toList();

	    List<Account> savedAccounts = accountRepo.saveAll(accounts);

	    List<Employee> employees = new ArrayList<>();

	    for (int i = 0; i < staffList.size(); i++) {
	        CreateStaffDTO dto = staffList.get(i);

	        Employee emp = new Employee();
	        emp.setAccount(savedAccounts.get(i));
	        emp.setFullName(dto.getFullName());
	        emp.setBranchCode(dto.getBranchCode());
	        emp.setJoiningDate(LocalDate.now());
	        emp.setDesignation(dto.getDesignation());

	        employees.add(emp);
	    }

	    return employeeRepo.saveAll(employees)
	            .stream()
	            .map(EmployeeListDTO::from)
	            .toList();
	}


//	@Override
//	public List<EmployeeListDTO> saveAllEmployees(List<CreateStaffDTO> staffList) {
//
//		List<Employee> employees = staffList.stream().map(dto -> {
//
//			Branch branch = branchRepo.findByBranchCodeAndActiveTrue(dto.getBranchCode())
//					.orElseThrow(() -> new CustomValidationException("Invalid Branch",
//							List.of(new FieldError("branchCode", "Branch not found or inactive"))));
//
//			Account account = new Account();
//
//			account.setEmail(dto.getEmail());
//			account.setPassword(passwordEncoder.encode(dto.getPassword()));
//			account.setBranch(branch);
//			account.setRole(dto.getRole());
//			account.setStatus(AccountStatus.ACTIVE);
//
//
//			Employee profile = new Employee();
//			profile.setAccount(account);
//			profile.setFullName(dto.getFullName());
//			profile.setBranchCode(dto.getBranchCode());
//			profile.setJoiningDate(LocalDate.now());
//			profile.setDesignation(dto.getDesignation());
//
//			return profile;
//		}).toList();
//
//		List<Employee> saved = employeeRepo.saveAll(employees);
//		System.out.println(employees);
//		return saved.stream().map(EmployeeListDTO::from).toList();
//	}

	// for getting employee swagger based
	@Override
	public List<Employee> getAllStaff() {
		return employeeRepo.findAll();
	}

	// changing status from dashboard
	@Override
	@Transactional
	public void updateStatusEmployee(Long employeeId, AccountStatus accountStatus) {

		Employee emp = employeeRepo.findById(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found"));
		 
		
		List<FieldError> errors = new ArrayList<>();

		//Employee employee = emp.get();

		// Account account = accountRepo.findById(id);
//		if (emp.isEmpty()) {
//
//			throw new RuntimeException("Account not found");
//		}

		Account acc = emp.getAccount();

		AccountStatus oldStatus = acc.getStatus(); 

		if(accountStatus == AccountStatus.PENDING_KYC || accountStatus == AccountStatus.REJECTED) {

			throw new  CustomValidationException("Can't change Employee to " + accountStatus);
		}
		acc.setStatus(accountStatus);
		
		//auditLogService.log(AuditAction.EMPLOYEE_STATUS_UPDATED, null, employeeId, null, null, null);
		
		auditLogService.log(
			    AuditAction.EMPLOYEE_STATUS_UPDATED,  
			    acc.getId(),
			    "EMPLOYEE",
			    oldStatus.name(),
			    accountStatus.name()
			);
		
		accountRepo.save(acc);
		
	}

	// changing designation from admin dashboard
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

	// paginated response

	@Override
	public AdminEmployeeResponseDTO getEmployees(int page, int size, Long branchId, String sortField, String sortOrder,
			AccountStatus status, Designation designation ) {

		Pageable pageable = PageRequest.of(page, size, EmployeeSortBuilder.build(sortField, sortOrder));

		Specification<Employee> specification = Specification.where(null);

		specification = specification.and(AccountSpecification.hasAccountStatus(status));

		specification = specification.and(AccountSpecification.hasDesignation(designation));

		specification = specification.and(AccountSpecification.hasbranch(branchId));


		Page<Employee> empPage = employeeRepo.findAll(specification, pageable);

		return AdminEmployeeResponseDTO.fromPage(empPage);
	}

	@Override
	public AdminUserTableResponseDTO getUsers(int page, int size, Long branchId ,String sortField, String sortOrder, AccountStatus status) {

		 Pageable pageable = PageRequest.of(page, size,
		           UserSortBulder.build(sortField, sortOrder));

//		 Page<Account> accPage ;
//
//		 if (status != null) {
//		     accPage = accountRepo.findByRoleAndStatus(Role.USER, status, pageable);
//		 }
//		 if (branchId != null) {
//			 accPage = accountRepo.findByBranchId(branchId,pageable);
//			 accPage = accountRepo.findByRoleAndBranchId(Role.USER,branchId,pageable);
//			 accPage = accountRepo.findByRoleAndStatusAndBranchId(Role.USER, status,branchId, pageable);
//		 }else {
//		     accPage = accountRepo.findByRole(Role.USER, pageable);
//		 }
//			Specification<Account> specification = Specification.where(null);
//
//		 specification = specification.and(AccountSpecification.hasbranchUser(branchId));

		 Specification<Account> spec =
		            Specification.where(AccountSpecification.hasRole(Role.USER))
		                    .and(AccountSpecification.hasbranchUser(branchId))
		                    .and(AccountSpecification.hasUserStatus(status));

		    Page<Account> accPage = accountRepo.findAll(spec, pageable);


		    return AdminUserTableResponseDTO.fromPage(accPage);

	}

	@Override
	public AdminUserResponseDTO getUserDetails(Long accountId) {
		
		Account account = accountRepo.findById(accountId)
				.orElseThrow(( )-> new RuntimeException("User not found"));
		
		// TODO Auto-generated method stub
		return null;
	}
	
	



//	public List<AdminEmployeeResponseDTO> getAllEmployees() {
//
//		return employeeRepo.findAll().stream()
//				.map(emp -> new AdminEmployeeResponseDTO(emp.getEmployeeId(), emp.getFullName(),
//						emp.getAccount().getEmail(), emp.getBranchCode(), emp.getDesignation(),
//						emp.getAccount().getStatus(), emp.getJoiningDate()))
//				.toList();
//	}

	// updating
	@Override
	@Transactional
	public void updateEmployee(Long employeeId, AccountUpdateRequestDTO req) {

//		Optional<Employee> employee = employeeRepo.findByEmployeeId(employeeId);


//		Employee emp = employee.get();
//		System.out.println(emp);

		 Employee emp = employeeRepo.findByEmployeeId(employeeId)
		            .orElseThrow(() -> new CustomValidationException("Employee not found"));

		    Account account = emp.getAccount();

		if(req.getStatus() == AccountStatus.PENDING_KYC || req.getStatus() == AccountStatus.REJECTED) {
			throw new  CustomValidationException("Can't change status to " + req.getStatus());
		}

		System.out.println(account);
		
		Map<String, Object> oldValues = new LinkedHashMap<>();
		Map<String, Object> newValues = new LinkedHashMap<>();
	

		if (req.getFullName() != null && !req.getFullName().equals(emp.getFullName())) {
			oldValues.put("fullName", emp.getFullName());
			newValues.put("fullName", req.getFullName());
			emp.setFullName(req.getFullName());
		}

		if (req.getBranchCode() != null && !req.getBranchCode().equals(emp.getBranchCode())) {
			oldValues.put("branchCode", emp.getBranchCode());
			newValues.put("branchCode", req.getBranchCode());
			emp.setBranchCode(req.getBranchCode());
		}

		if (req.getDesignation() != null && !req.getDesignation().equals(emp.getDesignation()) ) {
			oldValues.put("designation", emp.getDesignation());
			newValues.put("designation", req.getDesignation());
			emp.setDesignation(req.getDesignation());
		}

		if (req.getStatus() != null && !req.getStatus().equals(account.getStatus())) {
			oldValues.put("status", account.getStatus());
			newValues.put("status", req.getStatus());
			account.setStatus(req.getStatus());
		}
		
		
		if(!oldValues.isEmpty()) {
			auditLogService.log(
					AuditAction.EMPLOYEE_UPDATED,
					employeeId , 
					"EMPLOYEE",
					oldValues,
					newValues);
		}
		
	}

	@Override
	public void updateUser(Long userId, AccountUpdateRequestDTO req) {

		Account acc = accountRepo.findById(userId)
				.orElseThrow(() -> new CustomValidationException("User not found"));
		
		Map<String, Object> oldValues = new LinkedHashMap<>();
		Map<String, Object> newValues = new LinkedHashMap<>();

		if(req.getStatus() == AccountStatus.SUSPENDED ) {
			throw new  CustomValidationException("Can't change status to " + req.getStatus() + "Its only for Staff");
		}

		if (req.getStatus() != null && !req.getStatus().equals(acc.getStatus())) {
			oldValues.put("status", acc.getStatus());
			newValues.put("status", req.getStatus());
			acc.setStatus(req.getStatus());
		}


		NotificationRequestDTO userStatusNotification = new NotificationRequestDTO();
		userStatusNotification.setEmail(acc.getEmail());
		userStatusNotification.setPhone(String.valueOf(acc.getContact()));
		userStatusNotification.setSubject(AccountStatusEmailTemplate.getSubject(req.getStatus()));
		userStatusNotification.setMessage(

				 AccountStatusEmailTemplate.getBody(
					        req.getStatus(),
					        acc.getAccountHolderName(),
					        "Secure Bank",
					        "support@mybank.com"
					    )
				);
		notificationService.sendTransactionNotification(userStatusNotification);
		
		 auditLogService.log(AuditAction.USER_STATUS_UPDATED, userId,"USER", oldValues, newValues);

	}

	


}
