package com.example.bankapp.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bankapp.DTO.AdminUserTransactionCardResponseDTO;
import com.example.bankapp.DTO.GlobalAPIResponseDTO;
import com.example.bankapp.DTO.NotificationRequestDTO;
import com.example.bankapp.DTO.TransactionHistoryResponseDTO;
import com.example.bankapp.DTO.TransactionReqDTO;
import com.example.bankapp.DTO.TransactionResponseDTO;
import com.example.bankapp.DTO.TransferRequestDTO;
import com.example.bankapp.Exception.CustomValidationException;
import com.example.bankapp.Exception.FieldError;
import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.Transaction;
import com.example.bankapp.enums.AccountStatus;
import com.example.bankapp.enums.TransactionStatus;
import com.example.bankapp.repository.AccountRepo;
import com.example.bankapp.repository.TransactionRepo;
import com.example.bankapp.services.JWTservices;
import com.example.bankapp.services.NotificationService;
import com.example.bankapp.services.TransactionService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public  class TransactionServiceImp implements TransactionService {

	@Autowired
	AccountRepo accountRepo;

	@Autowired
	private JWTservices jService;

	@Autowired
	AuthenticationManager authManage;

	@Autowired
	TransactionRepo transactionRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private NotificationService notificationService;

//	@Override
//	public List<TransactionResponseDTO> getTransactionHistoryByAccountNum(String accountNumber) {
//
//		List<Transaction> transactions = transactionRepo.findByAccount_AccountNumberOrderByTimestampDesc(accountNumber);
//
//		return transactions.stream().map(TransactionResponseDTO::from).collect(Collectors.toList());
//	}

	@Override
	public List<TransactionResponseDTO> getTransactionHistoryByAccountNum(HttpServletRequest request) {

		String token = jService.extractTokenFromRequest(request);
		String accountNumber = jService.extractAccountNumber(token);

		List<Transaction> transactions = transactionRepo.findByAccount_AccountNumberOrderByTimestampDesc(accountNumber);

		return transactions.stream().map(TransactionResponseDTO::from).collect(Collectors.toList());
	}
	
	
	private void saveFailedTransaction(Account from, Account to, TransferRequestDTO request, TransactionStatus status,
			String reason, String counterPartyName) {
		Transaction failedTransaction = new Transaction();
		failedTransaction.setAccount(from); // Save fromAccount for audit
		failedTransaction.setType("Transfer");
		failedTransaction.setAmount(request.getAmount());
		failedTransaction.setBeforebalance(from != null ? from.getBalance() : 0.0);
		failedTransaction.setAfterbalance(from != null ? from.getBalance() : 0.0);
		failedTransaction.setTimestamp(LocalDateTime.now());
		failedTransaction.setDescription(reason);
		failedTransaction.setDirection("DEBIT");
		failedTransaction.setStatus(status);
		failedTransaction.setCounterPartyName(counterPartyName);

		transactionRepo.save(failedTransaction);
	}
	
	
	@Override
	public AdminUserTransactionCardResponseDTO cardHistory(String accountNumber) {
		
		Long total = transactionRepo.countByAccount_AccountNumber(accountNumber);
		Long debitCount = transactionRepo.countByAccount_AccountNumberAndDirection(accountNumber, "DEBIT");
		Long creditCount = transactionRepo.countByAccount_AccountNumberAndDirection(accountNumber, "CREDIT");
		
		Long successCount = transactionRepo
			    .countByAccount_AccountNumberAndStatus(accountNumber, TransactionStatus.COMPLETED);

			Long failedCount = transactionRepo
			    .countByAccount_AccountNumberAndStatus(accountNumber, TransactionStatus.FAILED);

			//Long pendingCount = transactionRepo.countByAccount_AccountNumberAndStatus(accountNumber, TransactionStatus.COMPLETED);

		System.out.println("Total debit counts: "+debitCount);
		System.out.println("Total credit counts: "+creditCount);
		
		AdminUserTransactionCardResponseDTO response = new AdminUserTransactionCardResponseDTO(total , creditCount, debitCount,failedCount, successCount );
		return response;
	}

//	@Override
//	public Page<TransactionResponseDTO> getTransactions(String accountNumber, int page, int size) {
//		Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
//
//		 Page<Transaction> transactions =
//	                transactionRepo.findByAccount_AccountNumber(accountNumber, pageable);
//
//	        // convert each Transaction to TransactionResponseDTO
//	        return transactions.map(TransactionResponseDTO::from);
//	}


	//  Here Direction means ascending and decending &

	@Override
	public TransactionHistoryResponseDTO getTransactions(String accountNumber, int page, int size, String sortFeid,
			String sortDirection) {

		Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortFeid).descending()
				: Sort.by(sortFeid).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);

		Page<Transaction> transactionsPage = transactionRepo.findByAccount_AccountNumber(accountNumber, pageable);

		List<TransactionResponseDTO> dtos = transactionsPage.getContent().stream().map(TransactionResponseDTO::from)
				.toList();

		Long total = transactionRepo.countByAccount_AccountNumber(accountNumber);
		Long debitCount = transactionRepo.countByAccount_AccountNumberAndDirection(accountNumber, "DEBIT");
		Long creditCount = transactionRepo.countByAccount_AccountNumberAndDirection(accountNumber, "CREDIT");

		System.out.println("Total debit counts: "+debitCount);
		System.out.println("Total credit counts: "+creditCount);

		return new TransactionHistoryResponseDTO(dtos, total, debitCount, creditCount, transactionsPage.getNumber(),
				transactionsPage.getTotalPages());
	}
	
	@Override
	public List<Transaction> getTransactionByDateRange(String accountNumber, LocalDate fromDate, LocalDate toDate) {
		LocalDateTime startDateTime = fromDate.atStartOfDay();
		LocalDateTime endDateTime = toDate.plusDays(1).atStartOfDay();
		return transactionRepo.findByAccountAndDateRange(accountNumber, startDateTime, endDateTime);
	}


	@Override
	@Transactional
	public ResponseEntity<?> transferMoney(String fromAccountNumber, TransferRequestDTO request) {

		Optional<Account> toOptionalAcc = accountRepo.findByAccountNumber(request.getToAccountNumber());
		
		Account toAcc = toOptionalAcc.get();

		Account fromAccount = accountRepo.findByAccountNumber(fromAccountNumber)
				.orElseThrow(() -> new RuntimeException("Sender  does not Exist"));

		if(fromAccount.getStatus().equals(AccountStatus.PENDING_KYC)){
			saveFailedTransaction(fromAccount, null, request, TransactionStatus.FAILED,
					"KYC Pending", request.getToAccountNumber());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalAPIResponseDTO<>("KYC Pending", false));
		}
		
		//		Account toAccount = accountRepo.findByAccountNumber(request.getToAccountNumber())
//				.orElseThrow(() -> new RuntimeException("Receiver Account does not Exist"));

		if (toOptionalAcc.isEmpty()) {
			saveFailedTransaction(fromAccount, null, request, TransactionStatus.FAILED,
					"Receiver account does not exist", request.getToAccountNumber());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalAPIResponseDTO<>("Receiver  does not exist", false));
		}

		double amount = request.getAmount();
		LocalDateTime now = LocalDateTime.now();

		Account toAccount = toOptionalAcc.get();

		if (fromAccount.equals(toAccount)) {
			saveFailedTransaction(fromAccount, toAccount, request, TransactionStatus.FAILED,
					"Can't transfer to same account", toAccount.getAccountHolderName());
			 List<FieldError> errors = List.of(
				        new FieldError("toAccount" ,
				        "You cannot transfer to your own account " + toAccount.getAccountNumber()));

				            throw new CustomValidationException(
				                "Can't Transfer to same Account",
				                errors);

//			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//					.body(new GlobalAPIResponseDTO<>("You cannot transfer to your account", false));
		}

		// check pin
		if (!passwordEncoder.matches(request.getPin(), fromAccount.getPin())) {
			saveFailedTransaction(fromAccount, toAccount, request, TransactionStatus.FAILED, "Wrong Pin",
					toAccount.getAccountHolderName());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalAPIResponseDTO<>("Incorrect PIN", false));
		}

		// check balance
		if (fromAccount.getBalance() < request.getAmount()) {
			saveFailedTransaction(fromAccount, toAccount, request, TransactionStatus.FAILED, "Insufficient Balance",
					toAccount.getAccountHolderName());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalAPIResponseDTO<>("Insufficient Balance", false));

			// throw new RuntimeException("Insufficient Balance");
		}

		// transaction record for sender account
		double beforeBalanceSender = fromAccount.getBalance();
		double afterBalanceSender = fromAccount.getBalance() - request.getAmount();

		// transaction record for receiver account
		double beforeBalanceReceiver = toAccount.getBalance();
		double afterBalanceReceiver = toAccount.getBalance() + request.getAmount();

		// perform transaction
		fromAccount.setBalance(fromAccount.getBalance() - request.getAmount());
		toAccount.setBalance(toAccount.getBalance() + request.getAmount());

		// transaction record for receiver
		accountRepo.save(fromAccount);
		accountRepo.save(toAccount);

		String description = request.getDescription();

		// transaction record for sender
		Transaction transactionForSender = new Transaction();
		transactionForSender.setAccount(fromAccount);
		transactionForSender.setType("Transfer");
		transactionForSender.setAmount(request.getAmount());
		transactionForSender.setBeforebalance(beforeBalanceSender);
		transactionForSender.setAfterbalance(afterBalanceSender);
		transactionForSender.setTimestamp(LocalDateTime.now());
		transactionForSender
				.setDescription(description != null ? description : "Transfer to " + toAccount.getAccountNumber());
		transactionForSender.setDirection("DEBIT");
		transactionForSender.setCounterPartyName(toAccount.getAccountHolderName());
		transactionForSender.setStatus(TransactionStatus.COMPLETED);

		// transaction record for receiver
		Transaction transactionForReceiver = new Transaction();
		transactionForReceiver.setAccount(toAccount);
		transactionForReceiver.setType("Transfer");
		transactionForReceiver.setAmount(request.getAmount());
		transactionForReceiver.setBeforebalance(beforeBalanceReceiver);
		transactionForReceiver.setAfterbalance(afterBalanceReceiver);
		transactionForReceiver.setTimestamp(LocalDateTime.now());
		transactionForReceiver
				.setDescription(description != null ? description : "Transfer from " + fromAccount.getAccountNumber());
		transactionForReceiver.setDirection("CREDIT");
		transactionForReceiver.setCounterPartyName(fromAccount.getAccountHolderName());
		transactionForReceiver.setStatus(TransactionStatus.COMPLETED);

		transactionRepo.save(transactionForSender);
		transactionRepo.save(transactionForReceiver);

		// Sending Notifications
		String time = transactionForSender.getTimestamp().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));

		TransactionResponseDTO dto = new TransactionResponseDTO();

		String successMessage = "Transfer successful from Account " + fromAccount.getAccountNumber() + " to Account "
				+ toAccount.getAccountNumber();

		// Sender notification
		NotificationRequestDTO senderNotification = new NotificationRequestDTO();
		senderNotification.setEmail(fromAccount.getEmail());
		senderNotification.setPhone(String.valueOf(fromAccount.getContact()));
		senderNotification.setSubject("₹" + amount + " Debited from Your Account");
		senderNotification.setMessage("₹" + amount + " has been debited from your account (A/c: "
				+ fromAccount.getAccountNumber() + ") on " + time + " to A/c " + toAccount.getAccountNumber()
				+ ". Available balance: ₹" + afterBalanceSender);
		notificationService.sendTransactionNotification(senderNotification);

		// Receiver notification
		NotificationRequestDTO receiverNotification = new NotificationRequestDTO();
		receiverNotification.setEmail(toAccount.getEmail());
		receiverNotification.setPhone(String.valueOf(toAccount.getContact())); // Long → String
		receiverNotification.setSubject("₹" + amount + " Credited to Your Account");
		receiverNotification.setMessage("₹" + amount + " has been credited to your account (A/c: "
				+ toAccount.getAccountNumber() + ") on " + time + " from A/c " + fromAccount.getAccountNumber()
				+ ". Available balance: ₹" + afterBalanceReceiver);
		notificationService.sendTransactionNotification(receiverNotification);

		return ResponseEntity.ok(new GlobalAPIResponseDTO<>(successMessage, true));
	}

	// deposit and withdraw are for practice purpose
	@Override
	public String depositAmount(TransactionReqDTO request) {

		Optional<Account> byId = accountRepo.findByAccountNumber(request.getAccountNumber());

		if (byId.isEmpty()) {
			throw new RuntimeException("Account Not Exist");
		}

		Account account = byId.get();
		if (!account.getPin().equals(request.getPin())) {
			throw new RuntimeException("Pin is Incorect");
		}

		double beforebalance = account.getBalance();
		double afterbalance = account.getBalance() + request.getAmount();

		// update balance
		account.setBalance(account.getBalance() + request.getAmount());
		accountRepo.save(account);

		// creating transaction record
		Transaction transaction = new Transaction();
		transaction.setAccount(account);
		transaction.setType("Deposit");
		transaction.setAmount(request.getAmount());
		transaction.setBeforebalance(beforebalance);
		transaction.setAfterbalance(afterbalance);
		transaction.setTimestamp(LocalDateTime.now());
		transaction.setDescription("Deposit to Account");

		transactionRepo.save(transaction);
		return "Succesfully Deposit. NetBalance : ₹" + account.getBalance();
	}

	@Override
	public String withdrawAmount(TransactionReqDTO request) {

		Optional<Account> accountopt = accountRepo.findByAccountNumber(request.getAccountNumber());
		if (accountopt.isEmpty()) {

			throw new RuntimeException("Account Not Exist");
		}

		Account account = accountopt.get();

		if (!account.getPin().equals(request.getPin())) {

			throw new RuntimeException("Pin is Inccorect!");

		}

		double beforebalance = account.getBalance();
		double afterbalance = account.getBalance() + request.getAmount();

		account.setBalance(account.getBalance() - request.getAmount());
		accountRepo.save(account);

		// creating transaction record
		Transaction transaction = new Transaction();
		transaction.setAccount(account);
		transaction.setType("Deposit");
		transaction.setAmount(request.getAmount());
		transaction.setBeforebalance(beforebalance);
		transaction.setAfterbalance(afterbalance);
		transaction.setTimestamp(LocalDateTime.now());
		transaction.setDescription("Deposit to Account");

		transactionRepo.save(transaction);

		return "Withdrawal successful. New balance: ₹" + account.getBalance();

	}

}
