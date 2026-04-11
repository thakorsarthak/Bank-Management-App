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

	boolean existsByContact(String contact);

	boolean existsByaadharNo(String aadharNo);

	boolean existsByPanNo(String panNo);

	@Override
	Optional<Account>findById(Long id);

	Optional<Account> findByEmail(String email);

	Optional<Account> findByContact(String Contact);

	Optional<Account> findByAccountNumber(String accountNumber);

	//Optional<Account> findTopByOrderByAccountNumberDesc();

	Optional<Account> findTopByOrderByIdDesc();

	Page<Account> findByRole(Role role, Pageable pageable);

	Page<Account> findByBranchId(Long branchId, Pageable pageable);

	Page<Account> findByRoleAndStatus(Role role, AccountStatus status, Pageable pageable);

	Page<Account> findByRoleAndBranchId(Role role, Long branchId, Pageable pageable);

	Page<Account> findByRoleAndStatusAndBranchId(Role user, AccountStatus status,Long branchId, Pageable pageable);


	//Was Created to use in Login but not using it now
	@Query("SELECT a FROM Account a WHERE " +
		       "a.email = :id OR " +
		       "a.contact = :id OR " +
		       "a.aadharNo = :id OR " +
		       "a.panNo = :id")
	Optional<Account> findByIdentifier(@Param("id") String identifier);







}
