package com.example.bankapp.entity;

import java.time.LocalDate;

import com.example.bankapp.enums.Designation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "employee_profiles")
@Data
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long employeeId;

	@OneToOne
	@JoinColumn(name = "account_id" , nullable = false ,  unique = true )
	private Account account;

	@Column(nullable = false)
    private String fullName;

	@Column(nullable = false, length = 4, name = "branch_code")
    private String branchCode;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Designation designation;

	@Column(nullable = false)
    private LocalDate joiningDate;

}
