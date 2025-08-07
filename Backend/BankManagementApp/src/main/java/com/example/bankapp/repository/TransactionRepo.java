package com.example.bankapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.Transaction;
@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {

	List<Transaction> findByAccount_AccountNumberOrderByTimestampDesc(String accountNumber);

	// Fetch last 5 transactions (most recent first)
  //  List<Transaction> findTop5ByUserOrderByDateDesc(Account account);

    //  Fetch all transactions of a Account
    //List<Transaction> findByUser(Account account);

	List<Transaction>findByAccountAndDirection(Account acc, String direction);

}
