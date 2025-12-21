package com.example.bankapp.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistoryResponseDTO {

	private List<TransactionResponseDTO> response ;
	private Long totalTransactions;
	private Long debitCount;
	private Long creditCount;
	private int currentPage;
	private int totalPages;



}
