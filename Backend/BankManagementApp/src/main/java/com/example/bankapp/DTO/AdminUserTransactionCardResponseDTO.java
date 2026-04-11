package com.example.bankapp.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminUserTransactionCardResponseDTO {

	//private List<TransactionDTO> transaction;
		private Long total;
		private Long credited;
	    private Long debited;
	    private Long failed;
	    private Long success;
}
