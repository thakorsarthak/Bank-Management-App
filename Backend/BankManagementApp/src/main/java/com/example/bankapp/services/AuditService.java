package com.example.bankapp.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bankapp.entity.AuditLog;
import com.example.bankapp.enums.AuditAction;
import com.example.bankapp.repository.AuditRepo;

@Service
public class AuditService {

	@Autowired
	private AuditRepo auditRepo;

	public void log(AuditAction action, String performedBy, String roleType, Long targetId, String oldValue,
			String newValue) {
		
		AuditLog log = new AuditLog();
        log.setAction(action.name());
        log.setPerformedBy(performedBy);
        log.setTargetType(roleType);
        log.setTargetId(targetId);
        log.setOldValue(oldValue);
        log.setNewValue(newValue);
        log.setCreatedAt(LocalDateTime.now());

        auditRepo.save(log);

	}
}
