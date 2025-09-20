package com.example.bankapp.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankapp.DTO.AccountLoginDTO;
import com.example.bankapp.DTO.AccountRequestDTO;
import com.example.bankapp.DTO.AccountResponseDTO;
import com.example.bankapp.entity.Account;
import com.example.bankapp.services.AccountService;
import com.example.bankapp.services.JWTservices;

@RestController
@RequestMapping("/main")
public class MainController {

	@Autowired
	AccountService accountService;

	@Autowired
	JWTservices jwtService;

	

	// create account
	@PostMapping("/create")
	public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody AccountRequestDTO account) {
		AccountResponseDTO createAccount = accountService.createAccount(account);
		return ResponseEntity.status(HttpStatus.CREATED).body(createAccount);
	}

	@PostMapping("/login-account")
	public ResponseEntity<?> login(@RequestBody AccountLoginDTO acc) {
		String token = accountService.verify(acc);
		if (!"Failed".equals(token)) {

			Date expiryDate = jwtService.extractExpiration(token);
			Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("expiresAt", expiryDate.getTime());
		//	return ResponseEntity.ok(new TokenResponseDTO(token));
			return ResponseEntity.ok(response);
		} else {
			System.out.println("Wrong credentials");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invaid Credentials");
		}
	}

//	@GetMapping("/accountNumber")
//	public ResponseEntity<AccountResponseDTO> getAccountDetailByAccountNo(@PathVariable Long accountNumber) {
//		AccountResponseDTO accountDetailByAccountNo = accountService.getAccountDetailByAccountNo(accountNumber);
//		return ResponseEntity.ok(accountDetailByAccountNo);
//	}
//


	@GetMapping("/getallaccount")
	public List<Account> getAllAccount() {
		List<Account> listOfAccount = accountService.getAllAccountDetails();
		return listOfAccount;
	}
}
