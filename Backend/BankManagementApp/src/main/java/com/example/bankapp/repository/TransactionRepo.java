package com.example.bankapp.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.bankapp.entity.Transaction;
import com.example.bankapp.enums.TransactionDirection;
import com.example.bankapp.enums.TransactionStatus;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {

	List<Transaction> findByAccount_AccountNumberOrderByTimestampDesc(String accountNumber);

	// Fetch last 5 transactions (most recent first)
	// List<Transaction> findTop5ByUserOrderByDateDesc(Account account);
	// Fetch all transactions of a Account
	// List<Transaction> findByUser(Account account);

	Page<Transaction> findByAccount_AccountNumber(String accountNumber, Pageable pageable);

//	List<Transaction> findByAccountAndDirection(Account acc, String direction);

	long countByTimestampBetween(LocalDateTime start, LocalDateTime end);

	long countByDirectionAndTimestampBetween(TransactionDirection  direction, LocalDateTime start, LocalDateTime end);

	long countByStatusAndTimestampBetween(TransactionStatus status, LocalDateTime start, LocalDateTime end);

	
	@Query("SELECT COALESCE(SUM(t.amount),0) FROM Transaction t WHERE t.direction = :direction AND t.timestamp BETWEEN :start AND :end")
	double sumAmountByDirection(
	        @Param("direction") TransactionDirection direction,
	        @Param("start") LocalDateTime start,
	        @Param("end") LocalDateTime end
	);
	

	Long countByAccount_AccountNumberAndStatus(String accountNumber, TransactionStatus status);

	Long countByAccount_AccountNumber(String accountNumber);

	Long countByAccount_AccountNumberAndDirection(String accountNumber, TransactionDirection direction);

	@Query("SELECT t FROM Transaction t " + "WHERE t.account.accountNumber = :accountNumber "
			+ "AND t.timestamp >= :startDate " + "AND t.timestamp < :endDate " + "ORDER BY t.timestamp DESC")
	List<Transaction> findByAccountAndDateRange(@Param("accountNumber") String accountNumber,
			@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
