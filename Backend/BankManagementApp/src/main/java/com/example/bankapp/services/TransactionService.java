package com.example.bankapp.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.bankapp.DTO.AdminUserTransactionCardResponseDTO;
import com.example.bankapp.DTO.TransactionHistoryResponseDTO;
import com.example.bankapp.DTO.TransactionReqDTO;
import com.example.bankapp.DTO.TransactionResponseDTO;
import com.example.bankapp.DTO.TransferRequestDTO;
import com.example.bankapp.entity.Transaction;

import jakarta.servlet.http.HttpServletRequest;

public interface TransactionService {


	// List<TransactionResponseDTO> getTransactionHistoryByAccountNum(HttpServletRequest request);

	public TransactionHistoryResponseDTO  getTransactions(String accountNumber, int page, int size, String sortby , String sortDirection);

	// public Page<TransactionResponseDTO> getTransactions(String accountNumber, int page, int size);
	
	
	public AdminUserTransactionCardResponseDTO cardHistory(String accountNumber);

		String depositAmount(TransactionReqDTO request);

		// Account depositAmount(Long accountNumber,Double amount);old
		String withdrawAmount(TransactionReqDTO request);

		// Account withdrawAmount(Long accontNumber, Double amount);

		List<Transaction> getTransactionByDateRange(String accountNumber, LocalDate fromDate, LocalDate toDate);

		 ResponseEntity<?> transferMoney(String fromAccountNumber, TransferRequestDTO request);

}