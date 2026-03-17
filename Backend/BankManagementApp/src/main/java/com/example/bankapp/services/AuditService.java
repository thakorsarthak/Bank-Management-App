package com.example.bankapp.services;

import java.time.LocalDateTime;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.bankapp.config.CustomAcountDetails;
import com.example.bankapp.entity.AuditLog;
import com.example.bankapp.enums.AuditAction;
import com.example.bankapp.enums.AuditStatus;
import com.example.bankapp.enums.Role;
import com.example.bankapp.repository.AuditRepo;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditService {

	
	private final AuditRepo auditRepo;
	
	private final ObjectMapper objectMapper;

	public void log(AuditAction action, Long targetId , String targetType, Object oldValue,
			Object newValue) {
		
				try {
					
					Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
					
					CustomAcountDetails details = (CustomAcountDetails) authentication.getPrincipal();
					
						AuditLog log = new AuditLog();
						
						
						//performer 
						log.setPerformedBy(details.getUsername());
						log.setPerformerId(details.getId());
						
						String role = details.getAuthorities()
						        .iterator()
						        .next()
						        .getAuthority()
						        .replace("ROLE_", "");

						log.setRoleType(Role.valueOf(role));
						
						//log.setRoleType(Role.valueOf(details.getAuthorities().iterator().next().getAuthority()));
						
						// what action
						log.setAction(action);
						log.setStatus(AuditStatus.SUCCESS);// by default seuccess
						
						// target 
						log.setTargetId(targetId);
						log.setTargetType(targetType);
						
						
						//changes
						log.setOldValue(oldValue != null ? objectMapper.writeValueAsString(oldValue) : null);
						log.setNewValue(newValue != null ? objectMapper.writeValueAsString(newValue) : null);
						
						log.setCreatedAt(LocalDateTime.now());

						auditRepo.save(log);
					}
				catch 
					(Exception e) {
					
						// never let audit failure break main flow
					
					e.printStackTrace();
					
					 System.out.println("Audit failed: " + e.getMessage());
						//message is spending 
	 
				}

				}
	
//	public void log(AuditAction action, Long targetId, String targetType) {
//	    log(action, targetId, targetType, null, null);
//	}
		}

