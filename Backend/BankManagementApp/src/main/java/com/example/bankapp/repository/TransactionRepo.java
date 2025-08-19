package com.example.bankapp.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.Transaction;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {

	List<Transaction> findByAccount_AccountNumberOrderByTimestampDesc(String accountNumber);

	// Fetch last 5 transactions (most recent first)
	// List<Transaction> findTop5ByUserOrderByDateDesc(Account account);
	// Fetch all transactions of a Account
	// List<Transaction> findByUser(Account account);

	 Page<Transaction> findByAccount_AccountNumber(String accountNumber, Pageable pageable);
	
	List<Transaction> findByAccountAndDirection(Account acc, String direction);

	@Query("SELECT t FROM Transaction t " +
			"WHERE t.account.accountNumber = :accountNumber " +
			"AND t.timestamp >= :startDate " +
		       "AND t.timestamp < :endDate " +
			"ORDER BY t.timestamp DESC")
	List<Transaction>findByAccountAndDateRange( @Param("accountNumber") String accountNumber,
			@Param("startDate")  LocalDateTime startDate ,
			@Param("endDate")  LocalDateTime endDate);

}
