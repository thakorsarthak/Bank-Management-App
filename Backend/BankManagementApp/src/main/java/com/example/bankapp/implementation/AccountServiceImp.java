package com.example.bankapp.implementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.bankapp.DTO.AccountLoginDTO;
import com.example.bankapp.DTO.AccountRequestDTO;
import com.example.bankapp.DTO.AccountResponseDTO;
import com.example.bankapp.DTO.AccountUpdateRequestDTO;
import com.example.bankapp.DTO.ChangePinRequestDTO;
import com.example.bankapp.DTO.GlobalAPIResponseDTO;
import com.example.bankapp.DTO.SetPinWithOtpDTO;
import com.example.bankapp.Exception.CustomValidationException;
import com.example.bankapp.Exception.FieldError;
import com.example.bankapp.entity.Account;
import com.example.bankapp.enums.Branch;
import com.example.bankapp.enums.ProductType;
import com.example.bankapp.repository.AccountRepo;
import com.example.bankapp.repository.TransactionRepo;
import com.example.bankapp.services.AccountService;
import com.example.bankapp.services.JWTservices;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AccountServiceImp implements AccountService {

	@Autowired
	AccountRepo repo;

	@Autowired
	private JWTservices jService;

	@Autowired
	AuthenticationManager authManage;

	@Autowired
	TransactionRepo Trepo;



	@Autowired
    private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

	private String generateAccountNumber(String branchCode, String productCode) {
		// Format: [BranchCode(4)][ProductCode(2)][Year(2)][Sequence(4)] = 12 digits

		String year = String.valueOf(LocalDate.now().getYear()).substring(2); // "25" for 2025

		// Fetch the latest account and get sequence number
		Account lastAccount = repo.findTopByOrderByIdDesc().orElse(null);
		int nextSequence = 1;

		if (lastAccount != null && lastAccount.getAccountNumber() != null
				&& lastAccount.getAccountNumber().length() == 12) {
			String lastSeqStr = lastAccount.getAccountNumber().substring(8); // Last 4 digits
			try {
				nextSequence = Integer.parseInt(lastSeqStr) + 1;
			} catch (NumberFormatException e) {
				// fallback to 1
				nextSequence = 1;
			}
		}

		String sequence = String.format("%04d", nextSequence); // Pad with 0s

		return branchCode + productCode + year + sequence;
	}

	@Override
	public AccountResponseDTO createAccount(AccountRequestDTO accountdto) {

		List<FieldError> errors = new ArrayList<>();

		if (repo.existsByEmail(accountdto.getEmail())) {
			errors.add(new FieldError("email", "Acccount with this Email is already Exist pls login"));
		}

		if (repo.existsByContact(accountdto.getContact())) {
			errors.add(new FieldError("contact", "Acccount with this Contact is already Exist pls login"));
		}

		// System.out.println("PIN: " + accountdto.getPin());
		// System.out.println("Confirm PIN: " + accountdto.getConfirmPin());

		if (!accountdto.getPin().equals(accountdto.getConfirmPin())) {
			throw new RuntimeException("pin and confirm pin must be similar");
		}

		if (!errors.isEmpty()) {
			throw new CustomValidationException(errors);
		}

		String generatedAccountNumber = generateAccountNumber(accountdto.getBranchCode(), accountdto.getProductCode());

		Account account = new Account();

		Double balance = accountdto.getBalance();

		account.setAccountNumber(generatedAccountNumber);
		account.setAccountHolderName(accountdto.getAccountHolderName());
		account.setContact(accountdto.getContact());
		// account.setBalance(accountdto.getBalance());
		account.setBalance(balance != null ? balance : 10000.0);
		account.setBranchCode(accountdto.getBranchCode());
		account.setProductCode(accountdto.getProductCode());
		account.setEmail(accountdto.getEmail());
		account.setPin(encoder.encode(accountdto.getPin()));
		// account.setAccountType(accountdto.getAccountType());

		Account save = repo.save(account);

		AccountResponseDTO response = new AccountResponseDTO();
		response.setAccountNumber(save.getAccountNumber());
		response.setAccountHolderName(save.getAccountHolderName());
		response.setBalance(save.getBalance());
		response.setContact(save.getContact());
		response.setEmail(save.getEmail());
		// response.setAccountType(save.getAccountType());

		return response;

	}

	@Override
	public String verify(AccountLoginDTO account) {

		try {
			Authentication authentication = authManage
					.authenticate(new UsernamePasswordAuthenticationToken(account.getEmail(), account.getPin()));

			if (authentication.isAuthenticated()) {

				Optional<Account> optionAcc = repo.findByEmail(account.getEmail());

				if (optionAcc.isPresent()) {

					Account acc = optionAcc.get();
					String token = jService.generateToken(acc.getEmail(), acc.getAccountNumber());

					System.out.println("Token From verify: " + token);

				 String	accountNum= acc.getEmail();
					redisTemplate.opsForValue().set("session:" + accountNum , token, 60, TimeUnit.MINUTES); // TTL should match token

					return token;
				} else {
					System.out.println("Account not found in database after authentication.");
					return "failed";
				}
			}

			return "Failed"; // Should never reach here normally

		} catch (AuthenticationException ex) {
			// Optional: log the error
			System.out.println("Authentication failed: " + ex.getMessage());
			return "Failed";
		}
	}

	@Override
	public ResponseEntity<?> getAccountHolderName(String accountNumber) {

		Optional<Account> account = repo.findByAccountNumber(accountNumber);

		if (account.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new GlobalAPIResponseDTO<>("Account not found", false));
		}
		String name = account.get().getAccountHolderName();
		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("Sucsess", true, name));
	}

//	@Override
//	public ResponseEntity<?> getAccountHolderN(HttpServletRequest request) {
//
//		String token = jService.extractTokenFromRequest(request);
//	    String accountNumber = jService.extractAccountNumber(token);
//
//	    Optional<Account> account = repo.findByAccountNumber(accountNumber);
//
//		if (account.isEmpty()) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND)
//					.body(new GlobalAPIResponseDTO<>("Account not found", false));
//		}
//		String name = account.get().getAccountHolderName();
//		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("Sucsess", true, name));
//	}




//	@Override
//	public AccountResponseDTO getAccountDetailByAccountNo(String accountNumber) {
//
//		Optional<Account> account = repo.findByAccountNumber(accountNumber);
//
//		if (account.isEmpty()) {
//			throw new RuntimeException("Account doesn't Exist");
//		}
//
//		Account accountFound = account.get();
//
//		AccountResponseDTO response = new AccountResponseDTO();
//
//		response.setAccountHolderName(accountFound.getAccountHolderName());
//		response.setAccountNumber(accountFound.getAccountNumber());
//		response.setEmail(accountFound.getEmail());
//		response.setBalance(accountFound.getBalance());
//		response.setContact(accountFound.getContact());
//
//		response.setBranchCode(accountFound.getBranchCode());
//		response.setBranchName(Branch.getNameByCode(accountFound.getBranchCode()));
//
//		response.setProductCode(accountFound.getProductCode());
//		response.setProductType(ProductType.getNameByCode(accountFound.getProductCode()));

//		return response;
//
//	}



	@Override
	public AccountResponseDTO getAccountDetailByAccountNo(HttpServletRequest request) {

		String token = jService.extractTokenFromRequest(request);
	    String accountNumber = jService.extractAccountNumber(token);

	    System.out.println("Inside get AccountDetails (service)");

	    Optional<Account> account = repo.findByAccountNumber(accountNumber);

		if (account.isEmpty()) {
			throw new RuntimeException("Account doesn't Exist");
		}

		Account accountFound = account.get();

		AccountResponseDTO response = new AccountResponseDTO();

		response.setAccountHolderName(accountFound.getAccountHolderName());
		response.setAccountNumber(accountFound.getAccountNumber());
		response.setEmail(accountFound.getEmail());
		response.setBalance(accountFound.getBalance());
		response.setContact(accountFound.getContact());

		response.setBranchCode(accountFound.getBranchCode());
		response.setBranchName(Branch.getNameByCode(accountFound.getBranchCode()));

		response.setProductCode(accountFound.getProductCode());
		response.setProductType(ProductType.getNameByCode(accountFound.getProductCode()));

		System.out.println("Details fetched succesfully" + response);
		return response;
	}


	@Override
	public List<Account> getAllAccountDetails() {

		List<Account> allaccount = repo.findAll();

		return allaccount;
	}

//	@Override
//	public Account depositAmount(Long accountNumber, Double amount) {
//		Optional<Account> byId = repo.findById(accountNumber);
//		if (byId.isEmpty()) {
//			throw new RuntimeException("Account Not Exist");
//		}
//
//		Account accountFound = byId.get();
//		Double netBalance = accountFound.getBalance() + amount;
//		accountFound.setBalance(netBalance);
//		Account account = repo.save(accountFound);
//		return account;
//	}

	@Override
	public String changePinWithOldPin(ChangePinRequestDTO changePin) {

		Optional<Account> byId = repo.findByAccountNumber(changePin.getAccountNumber());

		if (byId.isEmpty()) {

			throw new RuntimeException("Account Doesn't Exist or Wrong Accouny Number");
		}

		Account account = byId.get();

		if (!account.getPin().equals(changePin.getOldPin())) {

			throw new RuntimeException("Old PIN is Incorect");
		}

		if (!changePin.getNewPin().equals(changePin.getConfirsmPin())) {

			throw new RuntimeException("PIN and confirm PIN must be similar");
		}

		account.setPin(changePin.getNewPin());

		repo.save(account);

		return "PIN changed Successfully";
	}

	@Override
	public ResponseEntity<?> ChangePinWithOtp(SetPinWithOtpDTO resetPin) {
		if (!resetPin.getNewPin().equals(resetPin.getConfirmPin())) {

			return ResponseEntity.badRequest()
					.body(new GlobalAPIResponseDTO<>("Pin and Confirm pin do not match", false));
		}

		Account account = null;
		if (resetPin.getEmail() != null) {

			account = repo.findByEmail(resetPin.getEmail())
					.orElseThrow(() -> new RuntimeException("No account found with this Email"));
		} else if (resetPin.getContact() != null) {

			String phoneNo = resetPin.getContact(); // remove non-digits

			if (phoneNo.length() > 10 && phoneNo.startsWith("91")) {
				phoneNo = phoneNo.substring(2);
			}
			// System.out.println(phoneNo);
			Long phoneNumber = Long.parseLong(phoneNo);
			// System.out.println(phoneNumber);

			account = repo.findByContact(phoneNumber)
					.orElseThrow(() -> new RuntimeException("No account found with this Contact No"));
		}

		if (account == null) {
			return ResponseEntity.badRequest().body(new GlobalAPIResponseDTO<>("Account not found", false));
		}

		account.setPin(passwordEncoder.encode(resetPin.getNewPin()));
		repo.save(account);
		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("PIN updated successfully", true));

	}

	@Override
	public AccountResponseDTO updateAccountDetails(AccountUpdateRequestDTO dto) {

		Optional<Account> optional = repo.findByAccountNumber(dto.getAccountNumber());

		if (optional.isEmpty()) {

			throw new RuntimeException("Account Number is Incorect or Account does not Exist");
		}

		Account account = optional.get();

		if (!account.getPin().equals(dto.getPin())) {

			throw new RuntimeException("PIN is incorect");
		}

		/*
		 * account.setAccount_hname(dto.getAccount_hname());
		 * account.setContact(dto.getContact()); account.setEmail(dto.getEmail());
		 */

		if (dto.getAccountHolderName() != null) {
			account.setAccountHolderName(dto.getAccountHolderName());
		}

		if (dto.getEmail() != null) {
			account.setEmail(dto.getEmail());
		}

		if (dto.getContact() != null) {
			account.setContact(dto.getContact());
		}

		/*
		 * if (dto.getAccountType() != null) {
		 * account.setAccountType(dto.getAccountType()); }
		 */

		Account updated = repo.save(account);

		AccountResponseDTO response = new AccountResponseDTO();

		response.setAccountNumber(updated.getAccountNumber());
		response.setAccountHolderName(updated.getAccountHolderName());
		response.setBalance(updated.getBalance());
		response.setContact(updated.getContact());
		response.setEmail(updated.getEmail());
		// response.setAccountType(updated.getAccountType());

		return response;

	}

	@Override
	public Boolean closeAccount(Long accountNumber) {
		Optional<Account> byId = repo.findById(accountNumber);

		if (byId.isPresent()) {

			repo.deleteById(accountNumber);
			return true;
		}
		return false;
	}



}
