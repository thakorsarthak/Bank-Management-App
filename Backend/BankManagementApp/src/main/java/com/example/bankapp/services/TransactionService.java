package com.example.bankapp.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.bankapp.DTO.TransactionReqDTO;
import com.example.bankapp.DTO.TransactionResponseDTO;
import com.example.bankapp.DTO.TransferRequestDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface TransactionService {


	// List<TransactionResponseDTO> getTransactionHistoryByAccountNum(String accountNumber);

	 List<TransactionResponseDTO> getTransactionHistoryByAccountNum(HttpServletRequest request);



		String depositAmount(TransactionReqDTO request);

		// Account depositAmount(Long accountNumber,Double amount);old
		String withdrawAmount(TransactionReqDTO request);

		// Account withdrawAmount(Long accontNumber, Double amount);

		

		 ResponseEntity<?> transferMoney(String fromAccountNumber, TransferRequestDTO request);

}