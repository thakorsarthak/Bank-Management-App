package com.example.bankapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bankapp.entity.AuditLog;

@Repository
public interface AuditRepo extends JpaRepository<AuditLog, Long> {
	

}
