package com.example.bankapp.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bankapp.entity.DashboardStats;

@Repository
public interface DashboardStatsRepo extends JpaRepository<DashboardStats, Long>{

	 Optional<DashboardStats> findByDate(LocalDate date);
	 
	 Optional<DashboardStats> findByLastUpdatedAtBetween(
		        LocalDateTime start,
		        LocalDateTime end
		);
}
