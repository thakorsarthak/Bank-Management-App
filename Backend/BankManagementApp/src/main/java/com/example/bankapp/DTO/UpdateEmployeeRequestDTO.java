package com.example.bankapp.DTO;

import com.example.bankapp.enums.AccountStatus;
import com.example.bankapp.enums.Designation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeRequestDTO {

	private String fullName;
	private String branchCode;
	private Designation designation;
	private AccountStatus  status;
}
