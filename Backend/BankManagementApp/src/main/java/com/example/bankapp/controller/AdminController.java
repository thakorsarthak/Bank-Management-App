package com.example.bankapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankapp.DTO.CreateStaffDTO;
import com.example.bankapp.services.AdminService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

	
	private final AdminService adminService;
	
	
	 @PostMapping("/staff")
	    @PreAuthorize("hasRole('ADMIN')") // Only admin can call
	 public ResponseEntity<String> createStaff(
	            @Valid @RequestBody CreateStaffDTO request) {

	        adminService.createEmployeeOrManager(request);
	        return ResponseEntity.ok("Staff created successfully");
	    }
	 
	 @GetMapping("/staff")
	    @PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity<?> getAllStaff() {
	        return ResponseEntity.ok(adminService.getAllStaff());
	    }
}
