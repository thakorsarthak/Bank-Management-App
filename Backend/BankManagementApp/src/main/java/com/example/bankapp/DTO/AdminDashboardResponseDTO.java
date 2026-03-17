package com.example.bankapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardResponseDTO {

	 private Stats employees;
	 private Stats users;

	    @Data
	    @AllArgsConstructor
	    @NoArgsConstructor
	    public static class Stats {
	        private long total;
	        private long active;
	        private long inactive;
	    }

}
