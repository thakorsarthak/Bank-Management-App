package com.example.bankapp.entity;

import java.time.LocalDateTime;

import com.example.bankapp.enums.AuditAction;
import com.example.bankapp.enums.AuditStatus;
import com.example.bankapp.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "audit_logs", indexes = {
	    @Index(name = "idx_target", columnList = "targetId"),
	    @Index(name = "idx_performer", columnList = "performerId"),
	    @Index(name = "idx_action", columnList = "action"),
	    @Index(name = "idx_createdAt", columnList = "createdAt"),
	    @Index(name = "idx_target_action", columnList = "targetId, action")
	})
public class AuditLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long auditId;

	private String performedBy; // employee or admin or user

	private Long performerId;

	@Enumerated(EnumType.STRING)
	private Role roleType;

	@Enumerated(EnumType.STRING)
	private AuditAction action; // update user , view user, changed status

	private String targetType; // user or employee

	private Long targetId;     // id

	@Column(columnDefinition = "JSON")
    private String oldValue;

	@Column(columnDefinition = "JSON")
    private String newValue;

    @Enumerated(EnumType.STRING)
    private AuditStatus status;

    private LocalDateTime createdAt;
}
