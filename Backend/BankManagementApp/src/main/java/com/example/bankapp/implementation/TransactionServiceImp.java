package com.example.bankapp.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.bankapp.DTO.GlobalAPIResponseDTO;
import com.example.bankapp.DTO.TransactionReqDTO;
import com.example.bankapp.DTO.TransactionResponseDTO;
import com.example.bankapp.DTO.TransferRequestDTO;
import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.Transaction;
import com.example.bankapp.enums.TransactionStatus;
import com.example.bankapp.repository.AccountRepo;
import com.example.bankapp.repository.TransactionRepo;
import com.example.bankapp.services.JWTservices;
import com.example.bankapp.services.TransactionService;

@Service
public class TransactionServiceImp implements TransactionService {

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

	@Override
	public List<TransactionResponseDTO> getTransactionHistoryByAccountNum(String accountNumber) {

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
	public ResponseEntity<?> transferMoney(String fromAccountNumber, TransferRequestDTO request) {

		Optional<Account> toOptionalAcc = accountRepo.findByAccountNumber(request.getToAccountNumber());

		Account fromAccount = accountRepo.findByAccountNumber(fromAccountNumber)
				.orElseThrow(() -> new RuntimeException("Sender Account does not Exist"));

//		Account toAccount = accountRepo.findByAccountNumber(request.getToAccountNumber())
//				.orElseThrow(() -> new RuntimeException("Receiver Account does not Exist"));

		if (toOptionalAcc.isEmpty()) {
			saveFailedTransaction(fromAccount, null, request, TransactionStatus.FAILED,
					"Receiver account does not exist", request.getToAccountNumber());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalAPIResponseDTO<>("Receiver Account does not exist", false));
		}

		Account toAccount = toOptionalAcc.get();

		if (fromAccount.equals(toAccount)) {
			saveFailedTransaction(fromAccount, toAccount, request, TransactionStatus.FAILED,
					"Can't transfer to same account", toAccount.getAccountHolderName());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalAPIResponseDTO<>("You cannot transfer to your account", false));
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
		double beforebalance = fromAccount.getBalance();
		double afterbalance = fromAccount.getBalance() - request.getAmount();

		// transaction record for sender account
		double beforebalance2 = toAccount.getBalance();
		double afterbalance2 = toAccount.getBalance() + request.getAmount();

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
		transactionForSender.setBeforebalance(beforebalance);
		transactionForSender.setAfterbalance(afterbalance);
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
		transactionForReceiver.setBeforebalance(beforebalance2);
		transactionForReceiver.setAfterbalance(afterbalance2);
		transactionForReceiver.setTimestamp(LocalDateTime.now());
		transactionForReceiver
				.setDescription(description != null ? description : "Transfer from " + fromAccount.getAccountNumber());
		transactionForReceiver.setDirection("CREDIT");
		transactionForReceiver.setCounterPartyName(fromAccount.getAccountHolderName());
		transactionForReceiver.setStatus(TransactionStatus.COMPLETED);

		transactionRepo.save(transactionForSender);
		transactionRepo.save(transactionForReceiver);

		TransactionResponseDTO dto = new TransactionResponseDTO();

		String successMessage = "Transfer successful from Account " + fromAccount.getAccountNumber() + " to Account "
				+ toAccount.getAccountNumber();

		return ResponseEntity.ok(new GlobalAPIResponseDTO<>(successMessage, true));
	}

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

	@Override
	public List<TransactionResponseDTO> getTransactionHistory(String accountNumber) {

		return null;
	}



}
