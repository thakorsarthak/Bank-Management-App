package com.example.bankapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDTO {
	private String accountHolderName;
	private Double balance;
	private String email;
	private String password;
	private String confirmPassword;
	private String pin;
	private String confirmPin;
	private Long contact;
	private String panNo;
	private String aadhaarNo;
	private String accountType;
	private String branchCode;
	private String productCode;
	
  private AddressDTO address;
	
	
}
