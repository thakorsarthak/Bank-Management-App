package com.example.bankapp.entity;

import java.time.LocalDateTime;

import com.example.bankapp.enums.TransactionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime timestamp;
	private String type;
	private Double amount;

	private String description;
	private Double beforebalance;
	private Double afterbalance;

	@Column(name = "counter_party_name")
	private String counterPartyName;


	@Column(nullable = false)
	private String direction;// debit and credit

	@ManyToOne
	@JoinColumn(name = "account_number", referencedColumnName = "account_number")
	private Account account;

	 @Enumerated(EnumType.STRING)
	    private TransactionStatus status;
}
