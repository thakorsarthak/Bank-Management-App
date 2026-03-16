package com.example.bankapp.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bankapp.Exception.FieldError;
import com.example.bankapp.entity.AuditLog;

import com.example.bankapp.enums.AuditStatus;
import com.example.bankapp.repository.AuditRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditService {

	
	private final AuditRepo auditRepo;

	public void log(AuditStatus status,String action,  String performedBy, Long performerId, String roleType, Long targetId, String oldValue,
			String newValue) {
		
	
				try {
						AuditLog log = new AuditLog();
						log.setPerformedBy(performedBy);
						log.setPerformerId(performerId);
						log.setTargetType(roleType);
						log.setTargetId(targetId);
						log.setAction(action);
						log.setOldValue(oldValue);
						log.setNewValue(newValue);
						log.setStatus(AuditStatus.SUCCESS);
						log.setCreatedAt(LocalDateTime.now());

						auditRepo.save(log);
					}
				catch 
					(Exception e) {
						// never let audit failure break main flow
					
						//message is spending 
	 
					}

				}
		}

