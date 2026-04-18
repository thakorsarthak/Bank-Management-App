package com.example.bankapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardCardDTO {
	
	 private long totalTransactions;
	    private long totalCreditTransactions;
	    private long totalDebitTransactions;
	    private long totalFailedTransactions;

	    private double totalCreditAmount;
	    private double totalDebitAmount;
	    private double totalTransactionAmount;
	    private double netFlow;

	    private double failureRate;


}
