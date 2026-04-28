package com.example.bankapp.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
import com.example.bankapp.DTO.DashboardCardDTO;
import com.example.bankapp.DTO.GlobalAPIResponseDTO;
import com.example.bankapp.DTO.UpdateEmployeeRequestDTO;
import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.DashboardStats;
import com.example.bankapp.enums.AccountStatus;
import com.example.bankapp.enums.Designation;
import com.example.bankapp.repository.AccountRepo;
import com.example.bankapp.repository.DashboardStatsRepo;
import com.example.bankapp.services.AdminService;
import com.example.bankapp.services.DashboardService;
import com.example.bankapp.services.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor // if we are using private final instead of autowired
@PreAuthorize("hasRole('ADMIN')") //Only admin can call
public class AdminController {

	private final AdminService adminService;

	private final AccountRepo accountrepo;

	private final TransactionService tService;
	
	private final DashboardService dashboardService;
	
	private final DashboardStatsRepo dashboardStatsRepo;


	@GetMapping("/dashBoardCard")
    public ResponseEntity<DashboardCardDTO> getDashboard() {
		DashboardStats stats = dashboardStatsRepo.findByDate(LocalDate.now())
	            .orElseThrow(() -> new RuntimeException("No data found"));

	    DashboardCardDTO dto = dashboardService.mapToDTO(stats);

	    return ResponseEntity.ok(dto);
    }

    @PostMapping("/refresh")
    public void refreshDashboard() {
        dashboardService.computeAndStoreStats();
    }

	@PostMapping("/employee/addStaff")
	// @PreAuthorize("hasRole('ADMIN')") // Only admin can call This is for single api
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
	        @RequestParam(required = false)Long branchId,
	        @RequestParam(defaultValue = "joiningDate") String sortField,
	        @RequestParam(defaultValue = "ASC") String sortOrder,
	        @RequestParam(required = false) AccountStatus status,
	        @RequestParam(required = false) Designation designation){
		AdminEmployeeResponseDTO dto =  adminService.getEmployees(page, size,branchId, sortField, sortOrder, status , designation);

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
	        @RequestParam(required = false)Long branchId,
	        @RequestParam(defaultValue = "createdAt") String sortField,
	        @RequestParam(defaultValue = "ASC") String sortOrder,
	        @RequestParam(required = false) AccountStatus status){

		AdminUserTableResponseDTO dto = adminService.getUsers(page, size,branchId, sortField, sortOrder, status);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		System.out.println(auth.getAuthorities());

		GlobalAPIResponseDTO<AdminUserTableResponseDTO> response =
				new	GlobalAPIResponseDTO<>("user fetched Successfully", true , dto);

	    return ResponseEntity.ok(response) ;
	}



	@PatchMapping("/employee/{employeeId}/updateStatus")
	public ResponseEntity<?> updateAccountStatus(@PathVariable Long employeeId,
			@RequestBody UpdateEmployeeRequestDTO request) {

		 adminService.updateStatusEmployee(employeeId, request.getStatus());

		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("Employee updated sucessfuly !", true));
	}

	//old one swagger api
	@PatchMapping("/employee/{accountId}/designation")
	public ResponseEntity<?> updateDesignation(@PathVariable Long accountId,
			@RequestBody UpdateEmployeeRequestDTO request) {
		System.out.println("Inside update Designation");
		adminService.updateDesignation(accountId, request.getDesignation());
		return ResponseEntity.ok("Employee designation updated successfully");
	}

	@PatchMapping("/employee/{employeeId}/updateAllDetails")
	public ResponseEntity<?> updateAllEmployee(@PathVariable Long employeeId,
			@RequestBody AccountUpdateRequestDTO emp) {

		System.out.println("In Update Whole Employee");
		adminService.updateEmployee(employeeId, emp);

		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("Employee updated sucessfuly !", true));
	}

	@PatchMapping("/user/{accountId}/viewUser")
	public ResponseEntity<?> viewUpdateUser(@PathVariable Long accountId , @RequestBody AdminUserResponseDTO user){

		return ResponseEntity.ok(adminService.getUserDetails(accountId));
	}


	@GetMapping("/user/{accountId}/transactionHistoryCard")
	public ResponseEntity<?> userTransactionCard(@PathVariable Long accountId){

		Optional<Account> accountOp = accountrepo.findById(accountId);

		Account acc = accountOp.get();

	   //	AdminUserTransactionCardResponseDTO res =  tService.cardHistory(acc.getAccountNumber());

		return ResponseEntity.ok(tService.cardHistory(acc.getAccountNumber()));

		//return ResponseEntity.ok(null);
	}


	@PatchMapping("/user/{accountId}/updateUser")
	public ResponseEntity<?> updateUserStatus(@PathVariable Long accountId,
			@RequestBody AccountUpdateRequestDTO user) {

		adminService.updateUser(accountId, user);

		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("User updated sucessfuly !", true));
	}

}
