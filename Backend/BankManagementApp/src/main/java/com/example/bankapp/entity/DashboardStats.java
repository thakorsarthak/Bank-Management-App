package com.example.bankapp.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Table(name = "dashboard_stats")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DashboardStats {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    // For which day this stats belongs
	    @Column(nullable = false, unique = true)
	    private LocalDate date;

	    // Transaction counts
	    private long totalTransactions;
	    private long totalCreditTransactions;
	    private long totalDebitTransactions;
	    private long totalFailedTransactions;

	    // Amounts
	    private double totalCreditAmount;
	    private double totalDebitAmount;
	    private double totalTransactionAmount;
	    private double netFlow;

	    // Metadata
	    private LocalDateTime lastUpdatedAt;

}
