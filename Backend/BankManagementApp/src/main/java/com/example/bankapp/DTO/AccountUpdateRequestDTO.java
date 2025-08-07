package com.example.bankapp.DTO;

import lombok.Data;

@Data

public class AccountUpdateRequestDTO {

	private String accountNumber;
	private String pin;
	private String accountHolderName;

	private String email;
	private String accountType;
	private Long contact;

}
