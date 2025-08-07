package com.example.bankapp.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.bankapp.DTO.TransactionReqDTO;
import com.example.bankapp.DTO.TransactionResponseDTO;
import com.example.bankapp.DTO.TransferRequestDTO;

public interface TransactionService {


	 List<TransactionResponseDTO> getTransactionHistoryByAccountNum(String accountNumber);




		String depositAmount(TransactionReqDTO request);

		// Account depositAmount(Long accountNumber,Double amount);old
		String withdrawAmount(TransactionReqDTO request);

		// Account withdrawAmount(Long accontNumber, Double amount);

		 List<TransactionResponseDTO> getTransactionHistory(String accountNumber);

		 ResponseEntity<?> transferMoney(String fromAccountNumber, TransferRequestDTO request);

}