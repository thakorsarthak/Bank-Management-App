package com.example.bankapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bankapp.entity.Account;

public interface AccountRepo extends JpaRepository<Account, Long> {

}
