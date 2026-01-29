package com.example.bankapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.bankapp.entity.Employee;
import com.example.bankapp.enums.AccountStatus;

public interface EmployeeRepo extends JpaRepository<Employee, Long> , JpaSpecificationExecutor<Employee>{
	Optional<Employee> findByAccountId(Long accountId);

	Optional<Employee> findByEmployeeId(Long employeeId);

    @Override
	long count();

    long countByAccountStatus(AccountStatus status);
	// findByAccountId(Long aacountID);




    }
