package com.example.bankapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bankapp.entity.Account;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {
	boolean existsByEmail(String email);

	boolean existsByContact(Long contact);

	Optional<Account> findByEmail(String email);

	Optional<Account> findByContact(Long Contact);

	Optional<Account> findByAccountNumber(String accountNumber);

	//Optional<Account> findTopByOrderByAccountNumberDesc();

	Optional<Account> findTopByOrderByIdDesc();

	
	
	
}
