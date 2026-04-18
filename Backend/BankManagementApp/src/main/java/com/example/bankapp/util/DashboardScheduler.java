package com.example.bankapp.util;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.bankapp.implementation.DashboardServiceImpl;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DashboardScheduler {

	  private final DashboardServiceImpl dashboardService;

	    // Runs every 4 hours
	    @Scheduled(cron = "0 0 */4 * * *")
	    public void updateDashboardStats() {
	        dashboardService.computeAndStoreStats();
	    }

}
