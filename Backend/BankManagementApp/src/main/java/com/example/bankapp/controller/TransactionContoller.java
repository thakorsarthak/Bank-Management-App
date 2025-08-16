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

import com.example.bankapp.DTO.GlobalAPIResponseDTO;
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
public class TransactionContoller {

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

	@GetMapping("/history")
	public ResponseEntity<?> transactionHistoryByAccNum(HttpServletRequest request) {

		List<TransactionResponseDTO> history = transactionService.getTransactionHistoryByAccountNum(request);
		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("Transaction history fetched Successfuly", true, history));
	}

	@PutMapping("/tranfer")
	public ResponseEntity<?> tranferAmount(@RequestBody @Valid TransferRequestDTO dto, HttpServletRequest httpRequest) {

		String token = jwtService.extractTokenFromRequest(httpRequest);
		String fromAccount = jwtService.extractAccountNumber(token);

		return transactionService.transferMoney(fromAccount, dto);
	}

	@GetMapping("/downloadTransactionHistory")
	public void downloadTransHistoryExcel(
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fromDate,
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate toDate , HttpServletRequest httpRequest,
			HttpServletResponse response) throws IOException {


		String token = jwtService.extractTokenFromRequest(httpRequest);
		String accountNumber = jwtService.extractAccountNumber(token);

		List<Transaction> transactions =  transactionService.getTransactionByDateRange(accountNumber,fromDate , toDate );

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
