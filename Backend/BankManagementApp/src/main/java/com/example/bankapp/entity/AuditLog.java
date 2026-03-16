package com.example.bankapp.entity;

import java.time.LocalDateTime;

import com.example.bankapp.enums.AuditStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AuditLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long auditId; 

	private String performedBy; // employee or admin or user
	
	private Long performerId; 
	
	private String action; // update user , view user, changed status 

	private String targetType; // user or employee

	private Long targetId;     // id      

    private String oldValue;  
    
    private String newValue;	
    
    @Enumerated(EnumType.STRING)
    private AuditStatus status;   

    private LocalDateTime createdAt;	
}
