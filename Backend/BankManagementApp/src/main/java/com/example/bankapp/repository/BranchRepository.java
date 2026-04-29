package com.example.bankapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bankapp.entity.Branch;

public interface BranchRepository extends JpaRepository<Branch, Long> {

	Optional<Branch> findByBranchCodeAndActiveTrue(String branchCode);

    boolean existsByBranchCode(String branchCode);
    
    boolean existsByBranchCodeOrIfscCode(String branchCode , String ifscCode );
}
