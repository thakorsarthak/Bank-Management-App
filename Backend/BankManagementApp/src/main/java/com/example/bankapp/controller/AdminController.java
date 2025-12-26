package com.example.bankapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankapp.DTO.CreateStaffDTO;
import com.example.bankapp.DTO.UpdateDesignationRequest;
import com.example.bankapp.DTO.UpdateStatusRequest;
import com.example.bankapp.entity.Account;
import com.example.bankapp.services.AdminService;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor //if we are using private final instead of autowired
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	
	private final AdminService adminService;
	
	
	 @PostMapping("/employee/addStaff")
	   // @PreAuthorize("hasRole('ADMIN')") // Only admin can call
	 public ResponseEntity<String> createStaff(
	            @Valid @RequestBody CreateStaffDTO request) {

	        adminService.createEmployeeOrManager(request);
	        return ResponseEntity.ok("Staff created successfully");
	    }
	 
	 @GetMapping("/employee/getAllStaff")
	  //  @PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity<?> getAllStaff() {
	        return ResponseEntity.ok(adminService.getAllStaff());
	    }
	 
	 @PatchMapping("/employee/{accountId}/updateStatus")
	 public ResponseEntity<?> updateAccountStatus(@PathVariable Long accountId , @RequestBody UpdateStatusRequest request){
		 
		adminService.updateStatus(accountId , request.getStatus());
		 
		 return ResponseEntity.ok("Account status updated successfully");
	 }
	 
	 @PatchMapping("/employee/{accountId}/designation")
	    public ResponseEntity<?> updateDesignation(
	            @PathVariable Long accountId,
	            @RequestBody UpdateDesignationRequest request) {
		 System.out.println("Inside update Designation");
		 adminService.updateDesignation(accountId, request.getDesignation());
	        return ResponseEntity.ok("Employee designation updated successfully");
	    }
	 
	 
	 
}

