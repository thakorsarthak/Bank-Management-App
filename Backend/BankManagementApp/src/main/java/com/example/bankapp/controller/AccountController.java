package com.example.bankapp.controller;

import java.util.Map;

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
import com.example.bankapp.DTO.RefreshTokenRequestDTO;
import com.example.bankapp.DTO.ResetPasswordWithOtpDTO;
import com.example.bankapp.DTO.ResetPinWithOtpDTO;
import com.example.bankapp.services.AccountService;
import com.example.bankapp.services.JWTservice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	AccountService aService;

	@Autowired
	JWTservice jwtService;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	
	@PostMapping("/refresh")
	 public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO request){
		
		Map<String, Object> response = aService.refreshToken(request.getRefreshToken());
		
		return ResponseEntity.ok(response);
		
	}

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

	// Login Password
	@PutMapping("/changePinWithOtp")
	public ResponseEntity<?> resetPinWithOtp(@RequestBody ResetPinWithOtpDTO resetPin) {

		return aService.ChangePinWithOtp(resetPin);
	}



	@PutMapping("/changePasswordWithOtp")
	public ResponseEntity<?> setPasswordWithOtp(@RequestBody ResetPasswordWithOtpDTO passwordWithOtpDTO ){

		return aService.ChangePasswordWithOtp(passwordWithOtpDTO);

	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {

		String token = jwtService.extractTokenFromRequest(request);
		
		Long accountId = jwtService.extractAccountId(token);
		
		System.out.println("-----> Inside logout");
		redisTemplate.delete("session:" + accountId);
		
		System.err.println("---->Token deleted from Redis");
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
