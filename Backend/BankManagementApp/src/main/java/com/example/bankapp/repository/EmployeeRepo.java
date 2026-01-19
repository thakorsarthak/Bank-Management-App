package com.example.bankapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.bankapp.entity.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Long> , JpaSpecificationExecutor<Employee>{
	Optional<Employee> findByAccountId(Long accountId);

	Optional<Employee> findByEmployeeId(Long employeeId);

	// findByAccountId(Long aacountID);

}
