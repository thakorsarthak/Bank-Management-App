package com.example.bankapp.services;

import com.example.bankapp.entity.DashboardStats;

public interface DashboardService {

	 public void computeAndStoreStats() ;
	 public DashboardStats getTodayStats();

}
