package com.example.bankapp.DTO;

import com.example.bankapp.enums.AccountStatus;

import lombok.Data;

@Data
public class UpdateStatusRequest {
	private AccountStatus status;

}
