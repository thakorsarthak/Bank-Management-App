package com.example.bankapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bankapp.entity.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {

}
