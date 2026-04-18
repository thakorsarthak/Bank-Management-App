package com.example.bankapp.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bankapp.DTO.DashboardCardDTO;
import com.example.bankapp.entity.DashboardStats;
import com.example.bankapp.enums.TransactionDirection;
import com.example.bankapp.enums.TransactionStatus;
import com.example.bankapp.repository.DashboardStatsRepo;
import com.example.bankapp.repository.TransactionRepo;
import com.example.bankapp.services.DashboardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DashboardServiceImpl implements DashboardService {

	private final TransactionRepo transactionRepo;

	private final DashboardStatsRepo dashboardStatsRepo;
	
	
	@Override
	public DashboardCardDTO mapToDTO(DashboardStats stats) {

	    DashboardCardDTO dto = new DashboardCardDTO();

	    dto.setTotalTransactions(stats.getTotalTransactions());
	    dto.setTotalCreditTransactions(stats.getTotalCreditTransactions());
	    dto.setTotalDebitTransactions(stats.getTotalDebitTransactions());
	    dto.setTotalFailedTransactions(stats.getTotalFailedTransactions());

	    dto.setTotalCreditAmount(stats.getTotalCreditAmount());
	    dto.setTotalDebitAmount(stats.getTotalDebitAmount());
	    dto.setTotalTransactionAmount(stats.getTotalTransactionAmount());
	    dto.setNetFlow(stats.getNetFlow());

	    double failureRate = stats.getTotalTransactions() == 0 ? 0 :
	            (double) stats.getTotalFailedTransactions() / stats.getTotalTransactions() * 100;

	    dto.setFailureRate(failureRate);

	    return dto;
	}


	@Override
	public void computeAndStoreStats() {
		LocalDate today = LocalDate.now();

        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        long totalTransactions = transactionRepo.countByTimestampBetween(start, end);

        long totalCredit = transactionRepo.countByDirectionAndTimestampBetween(TransactionDirection.CREDIT, start, end);

        long totalDebit = transactionRepo.countByDirectionAndTimestampBetween(TransactionDirection.DEBIT, start, end);

        long totalFailed = transactionRepo.countByStatusAndTimestampBetween(TransactionStatus.FAILED, start, end);

        double debitAmount = transactionRepo.sumAmountByDirection(
                TransactionDirection.DEBIT, start, end);

        double creditAmount = transactionRepo.sumAmountByDirection(
                TransactionDirection.CREDIT, start, end);

        double totalAmount = creditAmount + debitAmount;
        double netFlow = creditAmount - debitAmount;

        DashboardStats stats = dashboardStatsRepo.findByDate(today)
                .orElse(DashboardStats.builder().date(today).build());

        stats.setTotalTransactions(totalTransactions);
        stats.setTotalCreditTransactions(totalCredit);
        stats.setTotalDebitTransactions(totalDebit);
        stats.setTotalFailedTransactions(totalFailed);

        stats.setTotalCreditAmount(creditAmount);
        stats.setTotalDebitAmount(debitAmount);
        stats.setTotalTransactionAmount(totalAmount);
        stats.setNetFlow(netFlow);

        stats.setLastUpdatedAt(LocalDateTime.now());
        
        System.out.println(stats);

        dashboardStatsRepo.save(stats);

	}

	@Override
	public DashboardStats getTodayStats() {
		return dashboardStatsRepo.findByDate(LocalDate.now())
                .orElse(new DashboardStats());
	}

}
