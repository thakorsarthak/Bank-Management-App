package com.example.bankapp.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.bankapp.entity.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Long>{
	Optional<Employee> findByAccountId(Long accountId);

	Optional<Employee> findByEmployeeId(Long employeeId);

	// findByAccountId(Long aacountID);
	
	@Query("""
	        SELECT e FROM Employee e
	        JOIN e.account a
	        ORDER BY
	          CASE a.status
	            WHEN 'ACTIVE' THEN 1
	            WHEN 'INACTIVE' THEN 2
	            ELSE 3
	          END,
	          e.joiningDate DESC
	    """)
	    Page<Employee> sortByAccountStatus(Pageable pageable);
}
