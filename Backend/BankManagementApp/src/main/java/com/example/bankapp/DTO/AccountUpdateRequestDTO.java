package com.example.bankapp.DTO;

import com.example.bankapp.enums.AccountStatus;
import com.example.bankapp.enums.Designation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountUpdateRequestDTO {

	private String accountNumber;
	private String pin;
	private String accountHolderName;
	private String fullName;
	private String branchCode;
	private String email;
	private String accountType;
	private Long contact;
	private Designation designation;
	private AccountStatus  status;

}
