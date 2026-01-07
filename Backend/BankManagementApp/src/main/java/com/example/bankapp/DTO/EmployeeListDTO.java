package com.example.bankapp.DTO;

import java.time.LocalDate;
import java.util.List;

import com.example.bankapp.entity.Employee;
import com.example.bankapp.entity.Transaction;
import com.example.bankapp.enums.AccountStatus;
import com.example.bankapp.enums.Designation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeListDTO {

	private Long employeeId;
	private String fullName;
	private String email;
	private String branchCode;
	private Designation designation;
	private AccountStatus status;
	private LocalDate joiningDate;
	
	 
	public static EmployeeListDTO from (Employee employee) {
	    EmployeeListDTO dto = new EmployeeListDTO();

	    dto.setEmployeeId(employee.getEmployeeId());
	    dto.setFullName(employee.getFullName());
	    dto.setBranchCode(employee.getBranchCode());
	    dto.setDesignation(employee.getDesignation());
	    dto.setJoiningDate(employee.getJoiningDate());

	    // status and email comes from ACCOUNT table
	    dto.setEmail(employee.getAccount().getEmail());
	    dto.setStatus(employee.getAccount().getStatus());

	    return dto;
	}

}
