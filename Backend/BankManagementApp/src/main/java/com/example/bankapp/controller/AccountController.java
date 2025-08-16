package com.example.bankapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankapp.DTO.AccountResponseDTO;
import com.example.bankapp.DTO.AccountUpdateRequestDTO;
import com.example.bankapp.DTO.ChangePinRequestDTO;
import com.example.bankapp.DTO.SetPinWithOtpDTO;
import com.example.bankapp.services.AccountService;
import com.example.bankapp.services.JWTservices;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	AccountService aService;

	@Autowired
	JWTservices jwtService;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@PutMapping("/updateAccount")
	public ResponseEntity<AccountResponseDTO> updateAccount(@RequestBody AccountUpdateRequestDTO dto) {
		AccountResponseDTO details = aService.updateAccountDetails(dto);

		return ResponseEntity.ok(details);
	}

	@PutMapping("/change-pin")
	public ResponseEntity<String> changePin(@RequestBody @Valid ChangePinRequestDTO changePin) {

		String result = aService.changePinWithOldPin(changePin);

		return ResponseEntity.ok(result);
	}

	@PutMapping("/changePinWithOtp")
	public ResponseEntity<?> setPinWithOtp(@RequestBody SetPinWithOtpDTO resetPin) {

		return aService.ChangePinWithOtp(resetPin);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {
		String token = jwtService.extractTokenFromRequest(request);
		String accountNumber = jwtService.extractAccountNumber(token);

		redisTemplate.delete("session:" + accountNumber);

		return ResponseEntity.ok("Logged out successfully");
	}

//	@GetMapping("/{accountNo}")
//	public ResponseEntity<AccountResponseDTO> getAccountDetailByAccountNo(@PathVariable String accountNumber) {
//		AccountResponseDTO accountDetailByAccountNo = aService.getAccountDetailByAccountNo(accountNumber);
//		return ResponseEntity.ok(accountDetailByAccountNo);
//	}

	@GetMapping("/accountHolderName/{accountNumber}")
	public ResponseEntity<?> getAccountHolderName(@PathVariable String accountNumber) {

		return aService.getAccountHolderName(accountNumber);

	}

//	@GetMapping("/accountHolderName")
//	public ResponseEntity<?> getAccountHolderName(HttpServletRequest request) {
//
//		return aService.getAccountHolderN(request);
//
//	}

//	@GetMapping("/accountHolderDetail/{accountNumber}")
//	public ResponseEntity<AccountResponseDTO> getAccountHolderDetails(@PathVariable String accountNumber) {
//		System.out.println("detail fetch api triggered");
//
//		AccountResponseDTO accountDetailByAccountNo = aService.getAccountDetailByAccountNo(accountNumber);
//		return ResponseEntity.ok(accountDetailByAccountNo);
//	}

	@GetMapping("/accountHolderDetail")
	public ResponseEntity<AccountResponseDTO> getAccountHolderDetails(HttpServletRequest request) {
		System.out.println("Inside HolderDetail fetch api controller");
		AccountResponseDTO accountDetailByAccountNo = aService.getAccountDetailByAccountNo(request);
		return ResponseEntity.ok(accountDetailByAccountNo);
	}


	@DeleteMapping("/delete/{accountNumber}")
	public ResponseEntity<String> closeAccount(@PathVariable Long accountNumber) {

		Boolean isDeleted = aService.closeAccount(accountNumber);
		if (isDeleted) {

			return ResponseEntity.ok("Account with " + accountNumber + " deleted successfully");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account with" + accountNumber + "is not Exist");

	}
}
