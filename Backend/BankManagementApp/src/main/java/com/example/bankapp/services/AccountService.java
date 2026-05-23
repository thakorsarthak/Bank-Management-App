package com.example.bankapp.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.example.bankapp.DTO.AccountLoginDTO;
import com.example.bankapp.DTO.AccountResponseDTO;
import com.example.bankapp.DTO.AccountSignUpDTO;
import com.example.bankapp.DTO.AccountUpdateRequestDTO;
import com.example.bankapp.DTO.ChangePinRequestDTO;
import com.example.bankapp.DTO.ResetPasswordWithOtpDTO;
import com.example.bankapp.DTO.ResetPinWithOtpDTO;
import com.example.bankapp.entity.Account;

import jakarta.servlet.http.HttpServletRequest;

public interface AccountService {

	AccountResponseDTO createAccount(AccountSignUpDTO accountDto);
	
	Optional<Account> findByIdentifier(String identifier);
	
	Map<String, Object> verify(AccountLoginDTO account,HttpServletRequest request);
	// Account verify(Account acc);

	// AccountResponseDTO getAccountDetailByAccountNo(String accountNumber);

	AccountResponseDTO getAccountDetailByAccountNo(HttpServletRequest request);

	ResponseEntity<?> getAccountHolderName(String accountNumber);

	List<Account> getAllAccountDetails();
	// String loginAccount(Long accountNumber); old

	Boolean closeAccount(Long accountNumber);

	String changePinWithOldPin(ChangePinRequestDTO changePin);

	AccountResponseDTO updateAccountDetails(AccountUpdateRequestDTO dto);

	ResponseEntity<?> ChangePasswordWithOtp(ResetPasswordWithOtpDTO resestPassword);

	ResponseEntity<?> ChangePinWithOtp(ResetPinWithOtpDTO resestPin);

	
}
