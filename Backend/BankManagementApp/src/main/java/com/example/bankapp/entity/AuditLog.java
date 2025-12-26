package com.example.bankapp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
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
	Long id;

	private String action;
	
	private String performedBy;
	
	private String targetType;
	
	private Long targetId;          // employeeId 

    private String oldValue;
    private String newValue;

    private LocalDateTime createdAt;
}
