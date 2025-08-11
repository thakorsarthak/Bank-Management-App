package com.example.bankapp.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.bankapp.DTO.AccountLoginDTO;
import com.example.bankapp.DTO.AccountRequestDTO;
import com.example.bankapp.DTO.AccountResponseDTO;
import com.example.bankapp.DTO.AccountUpdateRequestDTO;
import com.example.bankapp.DTO.ChangePinRequestDTO;
import com.example.bankapp.DTO.SetPinWithOtpDTO;
import com.example.bankapp.entity.Account;

import jakarta.servlet.http.HttpServletRequest;

public interface AccountService {



	AccountResponseDTO createAccount(AccountRequestDTO accountDto);

	AccountResponseDTO getAccountDetailByAccountNo(String accountNumber);
	 
	AccountResponseDTO getAccountDetailByAccNo(HttpServletRequest request);

	ResponseEntity<?> getAccountHolderName(String accountNumber);

	List<Account> getAllAccountDetails();
	// String loginAccount(Long accountNumber); old

	Boolean closeAccount(Long accountNumber);

	String changePinWithOldPin(ChangePinRequestDTO changePin);

	AccountResponseDTO updateAccountDetails(AccountUpdateRequestDTO dto);

	

	ResponseEntity<?> ChangePinWithOtp(SetPinWithOtpDTO resestPin);

	String verify(AccountLoginDTO account);
	// Account verify(Account acc);
}
