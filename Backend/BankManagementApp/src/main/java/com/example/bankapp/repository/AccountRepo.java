package com.example.bankapp.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bankapp.entity.Account;
import com.example.bankapp.enums.AccountStatus;
import com.example.bankapp.enums.Role;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> , JpaSpecificationExecutor<Account> {

	@Override
	long count();

	long countByStatus(AccountStatus status);

	boolean existsByEmail(String email);

	boolean existsByContact(Long contact);

	boolean existsByAadhaarNo(String aadharNo);

	boolean existsByPanNo(String panNo);


	Optional<Account> findByEmail(String email);

	Optional<Account> findByContact(Long Contact);

	Optional<Account> findByAccountNumber(String accountNumber);

	//Optional<Account> findTopByOrderByAccountNumberDesc();

	Optional<Account> findTopByOrderByIdDesc();

	Page<Account> findByRole(Role role, Pageable pageable);

	Page<Account> findByRoleAndStatus(Role role, AccountStatus status, Pageable pageable);


	//Was Created to use in Login but not using it now
	@Query("SELECT a FROM Account a WHERE " +
		       "a.email = :id OR " +
		       "CAST(a.contact AS string) = :id OR " +
		       "a.aadhaarNo = :id OR " +
		       "a.panNo = :id")
	Optional<Account> findByIdentifier(@Param("id") String identifier);

}
