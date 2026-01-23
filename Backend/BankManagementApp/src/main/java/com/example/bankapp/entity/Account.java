package com.example.bankapp.entity;

import java.time.Instant;

import com.example.bankapp.enums.AccountStatus;
import com.example.bankapp.enums.AccountType;
import com.example.bankapp.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts", uniqueConstraints = { @UniqueConstraint(columnNames = "email"),
		@UniqueConstraint(columnNames = "contact"), @UniqueConstraint(columnNames = "aadhaar_number"),
		@UniqueConstraint(columnNames = "pan_number") })
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "account_number", unique = true, nullable = true, length = 20)
	private String accountNumber; // 12-digit generated account number

	@Column(name = "holder_name")
	private String accountHolderName;

	private Double balance;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false, name = "password") // login password
	private String password;

	@Column(name = "transaction_pin")
	private String pin;

	@Column(unique = true)
	private Long contact;

	@Column(name = "pan_number", length = 20)
	private String panNo;

	@Column(name = "aadhaar_number", length = 12)
	private String aadhaarNo;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AccountStatus status;

//	@Column(name = "branch_code")
//	private String branchCode;  "1001" for Ahmedabad

	@Enumerated(EnumType.STRING)
	private AccountType accountType;   // 01 for savings, 02 for current , 03 Student , O4 Senior Citizen , 05 salary

	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;
	
	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@PrePersist
	public void prePersist() {
		this.createdAt = Instant.now();
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	private Address address;

}
