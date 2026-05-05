package com.example.bankapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankapp.DTO.AccountUpdateRequestDTO;
import com.example.bankapp.DTO.AdminDashboardResponseDTO;
import com.example.bankapp.DTO.AdminEmployeeResponseDTO;
import com.example.bankapp.DTO.AdminUserResponseDTO;
import com.example.bankapp.DTO.AdminUserTableResponseDTO;
import com.example.bankapp.DTO.CreateStaffDTO;
import com.example.bankapp.DTO.GlobalAPIResponseDTO;
import com.example.bankapp.DTO.UpdateEmployeeRequestDTO;
import com.example.bankapp.enums.AccountStatus;
import com.example.bankapp.enums.Designation;
import com.example.bankapp.services.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor // if we are using private final instead of autowired
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class AdminController {

	private final AdminService adminService;

	@PostMapping("/employee/addStaff")
	@Operation(summary = "Admin can create a Single Employee")
	// @PreAuthorize("hasRole('ADMIN')") // Only admin can call
	public ResponseEntity<?> createStaff(@Valid @RequestBody CreateStaffDTO request) {

		adminService.createEmployeeOrManager(request);

		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("Employee Created Successfully", true));
	}

	@PostMapping("/employee/bulk")
	@Operation(summary = "Admin can create a Multiple Employee", 
	description = "The format should be JSON to create multiple employee")
	public ResponseEntity<?> bulkStaff(@RequestBody List<CreateStaffDTO> request) {

		adminService.saveAllEmployees(request);

		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("Staff Created Successfully", true));

	}

	@GetMapping("/employee/stats")
	@Operation(summary = "Total Employee and Users",
	description = "It fetch total Employee and User with filtered according to there Account Status")
	public ResponseEntity<?> getStats() {

		AdminDashboardResponseDTO data = adminService.getStats();

		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("Dashboard stats fetched", true, data));

	}

	@GetMapping("/employee/getAllEmployee")
	@Operation (summary = "To get All Employee Paginated Format" , description = "It's for Admin Dashboard")
	public ResponseEntity<GlobalAPIResponseDTO> getEmployees(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) Long branchId,
			@RequestParam(defaultValue = "joiningDate") String sortField,
			@RequestParam(defaultValue = "ASC") String sortOrder, @RequestParam(required = false) AccountStatus status,
			@RequestParam(required = false) Designation designation) {
		AdminEmployeeResponseDTO dto = adminService.getEmployees(page, size, branchId, sortField, sortOrder, status,
				designation);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		//System.out.println(auth.getAuthorities());
		
		GlobalAPIResponseDTO<AdminEmployeeResponseDTO> response = new GlobalAPIResponseDTO<>(
				"employee fetched Successfully", true, dto);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/user/getAllUser")
	@Operation (summary = "To get All User Paginated Format" , description = "It's for Admin Dashboard")
	public ResponseEntity<GlobalAPIResponseDTO> getUsers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) Long branchId,
			@RequestParam(defaultValue = "createdAt") String sortField,
			@RequestParam(defaultValue = "ASC") String sortOrder,
			@RequestParam(required = false) AccountStatus status) {

		AdminUserTableResponseDTO dto = adminService.getUsers(page, size, branchId, sortField, sortOrder, status);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		System.out.println(auth.getAuthorities());

		GlobalAPIResponseDTO<AdminUserTableResponseDTO> response = new GlobalAPIResponseDTO<>(
				"user fetched Successfully", true, dto);

		return ResponseEntity.ok(response);
	}

//	@GetMapping("/employee/getAllStaff")
//	// @PreAuthorize("hasRole('ADMIN')")
//	public ResponseEntity<?> getAllStaff() {
//		List<AdminEmployeeResponseDTO> response = adminService.getAllEmployees();
//		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("Employee fetched sucessfuly", true, response));
//	}

	@PatchMapping("/employee/{employeeId}/updateStatus")
	@Operation (summary = "Only for Updating status")
	public ResponseEntity<?> updateAccountStatus(@PathVariable Long employeeId,
			@RequestBody UpdateEmployeeRequestDTO request) {

		adminService.updateStatusEmployee(employeeId, request.getStatus());

		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("Employee updated sucessfuly !", true));
	}

	// old one swagger
	@PatchMapping("/employee/{accountId}/designation")
	@Operation (summary = "Only for Updating designation")
	public ResponseEntity<?> updateDesignation(@PathVariable Long accountId,
			@RequestBody UpdateEmployeeRequestDTO request) {
		//log.info("Inside update Designation");
		adminService.updateDesignation(accountId, request.getDesignation());
		return ResponseEntity.ok("Employee designation updated successfully");
	}

	@PatchMapping("/employee/{employeeId}/updateAllDetails")
	@Operation (summary = "Admin can See and update all Details of Employee")
	public ResponseEntity<?> updateAllEmployee(@PathVariable Long employeeId,
			@RequestBody AccountUpdateRequestDTO emp) {

		//log.warn("In Update Whole Employee");
		adminService.updateEmployee(employeeId, emp);

		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("Employee updated sucessfuly !", true));
	}

	@PatchMapping("/user/{accountId}/viewUser")
	@Operation (summary = "Admin can See and update status of User(swagger based)")
	public ResponseEntity<?> viewUpdateUser(@PathVariable Long accountId, @RequestBody AdminUserResponseDTO user) {

		return ResponseEntity.ok(adminService.getUserDetails(accountId));
	}

	@PatchMapping("/user/{accountId}/updateUser")
	@Operation (summary = "Admin can check and update status of User")
	public ResponseEntity<?> updateUserStatus(@PathVariable Long accountId, @RequestBody AccountUpdateRequestDTO user) {

		adminService.updateUser(accountId, user);

		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("User updated sucessfuly !", true));
	}

}
