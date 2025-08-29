package com.example.bankapp.DTO;

import java.util.List;

import com.example.bankapp.AddressDTO;

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
	private String panNumber;
	private String aadhaarNumber;
	private String accountType;
	private String branchCode;
	private String productCode;
	
   private AddressDTO address;
	
	
}
