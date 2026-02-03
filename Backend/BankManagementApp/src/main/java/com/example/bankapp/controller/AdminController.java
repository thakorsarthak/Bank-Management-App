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

import com.example.bankapp.DTO.AdminDashboardResponseDTO;
import com.example.bankapp.DTO.AdminEmployeeResponseDTO;
import com.example.bankapp.DTO.AdminUserResponseDTO;
import com.example.bankapp.DTO.CreateStaffDTO;
import com.example.bankapp.DTO.GlobalAPIResponseDTO;
import com.example.bankapp.DTO.UpdateDesignationRequest;
import com.example.bankapp.DTO.UpdateEmployeeRequestDTO;
import com.example.bankapp.DTO.UpdateStatusRequest;
import com.example.bankapp.enums.AccountStatus;
import com.example.bankapp.enums.Designation;
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

	@PostMapping("/employee/bulk")
	public ResponseEntity<?> bulkStaff(@RequestBody List<CreateStaffDTO> request){

		adminService.saveAllEmployees(request);

		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("Staff Created Successfully", true));

	}


	@GetMapping("/employee/stats")
	public ResponseEntity<?> getStats(){

		AdminDashboardResponseDTO data = adminService.getStats();

		return ResponseEntity.ok(
                new GlobalAPIResponseDTO<>("Dashboard stats fetched", true, data));


	}

	@GetMapping("/employee/getAllEmployee")
	public ResponseEntity<GlobalAPIResponseDTO> getEmployees(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "joiningDate") String sortField,
	        @RequestParam(defaultValue = "ASC") String sortOrder,
	        @RequestParam(required = false) AccountStatus status,
	        @RequestParam(required = false) Designation designation){
		AdminEmployeeResponseDTO dto =  adminService.getEmployees(page, size, sortField, sortOrder, status , designation);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		System.out.println(auth.getAuthorities());


		GlobalAPIResponseDTO<AdminEmployeeResponseDTO> response =
				new	GlobalAPIResponseDTO<>("employee fetched Successfully", true , dto);

	    return ResponseEntity.ok(response) ;
	}


	@GetMapping("/user/getAllUser")
	public ResponseEntity<GlobalAPIResponseDTO> getUsers(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "createdAt") String sortField,
	        @RequestParam(defaultValue = "ASC") String sortOrder,
	        @RequestParam(required = false) AccountStatus status){

		AdminUserResponseDTO dto = adminService.getUsers(page, size, sortField, sortOrder, status);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		System.out.println(auth.getAuthorities());

		GlobalAPIResponseDTO<AdminUserResponseDTO> response =
				new	GlobalAPIResponseDTO<>("user fetched Successfully", true , dto);

	    return ResponseEntity.ok(response) ;
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
