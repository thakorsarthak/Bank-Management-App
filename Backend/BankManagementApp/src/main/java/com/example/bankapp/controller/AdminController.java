package com.example.bankapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankapp.DTO.AdminEmployeeResponseDTO;
import com.example.bankapp.DTO.CreateStaffDTO;
import com.example.bankapp.DTO.GlobalAPIResponseDTO;
import com.example.bankapp.DTO.UpdateDesignationRequest;
import com.example.bankapp.DTO.UpdateEmployeeRequestDTO;
import com.example.bankapp.DTO.UpdateStatusRequest;
import com.example.bankapp.entity.Employee;
import com.example.bankapp.services.AdminService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor // if we are using private final instead of autowired
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	private final AdminService adminService;

	@PostMapping("/employee/addStaff")
	// @PreAuthorize("hasRole('ADMIN')") // Only admin can call
	public ResponseEntity<?> createStaff(@Valid @RequestBody CreateStaffDTO request) {

	 adminService.createEmployeeOrManager(request);
	
		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("Employee Created Successfully", true));
	}
	
	@GetMapping("/employee/getAllEmployee")
	public AdminEmployeeResponseDTO getAllEmployeePag( 
	        @RequestParam int page,
	        @RequestParam int size,
	        @RequestParam String sortField,
	        @RequestParam String sortOrder){
		
	//	AdminEmployeeResponseDTO dto = adminService.
	    return adminService.getEmployees(page, size, sortField, sortOrder);
		
	}

//	@GetMapping("/employee/getAllStaff")
//	// @PreAuthorize("hasRole('ADMIN')")
//	public ResponseEntity<?> getAllStaff() {
//		List<AdminEmployeeResponseDTO> response = adminService.getAllEmployees();
//		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("Employee fetched sucessfuly", true, response));
//	}

	@PatchMapping("/employee/{employeeId}/updateStatus")
	public ResponseEntity<?> updateAccountStatus(@PathVariable Long employeeId,
			@RequestBody UpdateStatusRequest request) {

		 adminService.updateStatusEmployee(employeeId, request.getStatus());

		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("Employee updated sucessfuly !", true));
	}

	@PatchMapping("/employee/{accountId}/designation")
	public ResponseEntity<?> updateDesignation(@PathVariable Long accountId,
			@RequestBody UpdateDesignationRequest request) {
		System.out.println("Inside update Designation");
		adminService.updateDesignation(accountId, request.getDesignation());
		return ResponseEntity.ok("Employee designation updated successfully");
	}

	@PatchMapping("/employee/{employeeId}/updateAllDetails")
	public ResponseEntity<?> updateAllEmployee(@PathVariable Long employeeId,
			@RequestBody UpdateEmployeeRequestDTO emp) {

		System.out.println("In Update Whole Employee");
		adminService.updateEmployee(employeeId, emp);

		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("Employee updated sucessfuly !", true, emp));
	}

}
