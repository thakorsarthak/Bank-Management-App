package com.example.bankapp.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankapp.DTO.AdminUserTransactionCardResponseDTO;
import com.example.bankapp.DTO.GlobalAPIResponseDTO;
import com.example.bankapp.DTO.TransactionHistoryResponseDTO;
import com.example.bankapp.DTO.TransactionReqDTO;
import com.example.bankapp.DTO.TransactionResponseDTO;
import com.example.bankapp.DTO.TransferRequestDTO;
import com.example.bankapp.entity.Transaction;
import com.example.bankapp.services.JWTservices;
import com.example.bankapp.services.TransactionService;
import com.example.bankapp.util.ExcelUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

	@Autowired
	TransactionService transactionService;

	@Autowired
	JWTservices jwtService;

//	@GetMapping("/history/{accountNumber}")
//	public ResponseEntity<?> transactionHistoryByAccNo(@PathVariable String accountNumber){
//
//		List<TransactionResponseDTO> history = tService.getTransactionHistoryByAccountNum(accountNumber);
//		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("Transaction history fetched Successfuly", true, history));
//	}


	@GetMapping("/cardHistory")
	public ResponseEntity<?> transactionCardHistoryByAccNum(HttpServletRequest request) {

		String token = jwtService.extractTokenFromRequest(request);
		String accountNo = jwtService.extractAccountNumber(token);

		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("Transaction history fetched Successfuly", true,
				transactionService.cardHistory(accountNo)));
	}

	@PutMapping("/transfer")
	public ResponseEntity<?> tranferAmount(@RequestBody @Valid TransferRequestDTO dto, HttpServletRequest httpRequest) {

		String token = jwtService.extractTokenFromRequest(httpRequest);
		String fromAccount = jwtService.extractAccountNumber(token);

		return transactionService.transferMoney(fromAccount, dto);
	}

//	@GetMapping("/downloadTransactionHistoryBypageNation")
//	public ResponseEntity<GlobalAPIResponseDTO> Pagenation(HttpServletRequest request,
//			@RequestParam int page, @RequestParam int size) {
//		String token = jwtService.extractTokenFromRequest(request);
//		String accountNumber = jwtService.extractAccountNumber(token);
//
//		Page<TransactionResponseDTO> dtoPage =
//	          transactionService.getTransactions(accountNumber, page, size,sortBy,sortByDirection);
//
//	    GlobalAPIResponseDTO<Page<TransactionResponseDTO>> response =
//	            new GlobalAPIResponseDTO<>("Transactions fetched successfully", true, dtoPage);
//
//	    return ResponseEntity.ok(response);
//	}

	@GetMapping("/transactionHistory")
	public ResponseEntity<GlobalAPIResponseDTO> Pagenation(HttpServletRequest request,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "timestamp") String sortByTime,
			@RequestParam(defaultValue = "desc") String sortByDirection) {
		String token = jwtService.extractTokenFromRequest(request);
		String accountNumber = jwtService.extractAccountNumber(token);

		TransactionHistoryResponseDTO dtoPage = transactionService.getTransactions(accountNumber, page, size,
				sortByTime, sortByDirection);

		GlobalAPIResponseDTO<TransactionHistoryResponseDTO> response = new GlobalAPIResponseDTO<>(
				"Transactions fetched successfully", true, dtoPage);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/downloadTransactionHistory")
	public void downloadTransHistoryExcel(@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fromDate,
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate toDate, HttpServletRequest httpRequest,
			HttpServletResponse response) throws IOException {

		String token = jwtService.extractTokenFromRequest(httpRequest);
		String accountNumber = jwtService.extractAccountNumber(token);

		List<Transaction> transactions = transactionService.getTransactionByDateRange(accountNumber, fromDate, toDate);

		// Set Excel headers
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=transactions.xlsx");

		// Write to Excel
		ExcelUtil.transactionExport(transactions, response.getOutputStream());

	}

	@PutMapping("/deposit")
	public ResponseEntity<String> depositAmount(@RequestBody TransactionReqDTO request) {
		String result = transactionService.depositAmount(request);

		return ResponseEntity.ok(result);
	}

	@PutMapping("/withdraw")
	public ResponseEntity<String> withDrawAmount(@RequestBody TransactionReqDTO request) {
		String result = transactionService.withdrawAmount(request);

		return ResponseEntity.ok(result);

	}

}
