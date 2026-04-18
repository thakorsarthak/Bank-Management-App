package com.example.bankapp.services;

import com.example.bankapp.DTO.DashboardCardDTO;
import com.example.bankapp.entity.DashboardStats;

public interface DashboardService {

	
	public DashboardCardDTO mapToDTO(DashboardStats stats);
	 public void computeAndStoreStats() ;
	 public DashboardStats getTodayStats();

}
