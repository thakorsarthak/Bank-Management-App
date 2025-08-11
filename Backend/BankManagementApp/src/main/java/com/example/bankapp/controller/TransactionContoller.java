package com.example.bankapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankapp.DTO.GlobalAPIResponseDTO;
import com.example.bankapp.DTO.TransactionReqDTO;
import com.example.bankapp.DTO.TransactionResponseDTO;
import com.example.bankapp.DTO.TransferRequestDTO;
import com.example.bankapp.services.JWTservices;
import com.example.bankapp.services.TransactionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/transaction")
public class TransactionContoller {


	@Autowired
	TransactionService tService;

	@Autowired
	JWTservices jwtService;

//	@GetMapping("/history/{accountNumber}")
//	public ResponseEntity<?> transactionHistoryByAccNo(@PathVariable String accountNumber){
//
//		List<TransactionResponseDTO> history = tService.getTransactionHistoryByAccountNum(accountNumber);
//		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("Transaction history fetched Successfuly", true, history));
//	}

	@GetMapping("/history")
	public ResponseEntity<?> transactionHistoryByAccNum(HttpServletRequest request){

		List<TransactionResponseDTO> history = tService.getTransactionHistoryByAccountNum(request);
		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("Transaction history fetched Successfuly", true, history));
	}

	


	@PutMapping("/tranfer")
	public ResponseEntity<?> tranferAmount(@RequestBody @Valid TransferRequestDTO dto ,  HttpServletRequest httpRequest) {

		String token = jwtService.extractTokenFromRequest(httpRequest);
	    String fromAccount= jwtService.extractAccountNumber(token);


		return tService.transferMoney(fromAccount,dto);
	}


	@PutMapping("/deposit")
	public ResponseEntity<String> depositAmount(@RequestBody TransactionReqDTO request) {
		String result = tService.depositAmount(request);

		return ResponseEntity.ok(result);
	}

	@PutMapping("/withdraw")
	public ResponseEntity<String> withDrawAmount(@RequestBody TransactionReqDTO request) {
		String result = tService.withdrawAmount(request);

		return ResponseEntity.ok(result);

	}

}
