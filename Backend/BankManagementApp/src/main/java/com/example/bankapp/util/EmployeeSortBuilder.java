package com.example.bankapp.util;

import java.util.Map;

import org.springframework.data.domain.Sort;

public final class EmployeeSortBuilder {

	private  EmployeeSortBuilder() {
		// TODO Auto-generated constructor stub
	}
	
	private static final Map<String , String> FEIED_MAP = Map.of(
			"status", "account_status",
			"joiningDate", "joiningDate",
			"designation","designation"
			);
			
			
	public static Sort build(String field , String order) {
		
		String mappedfield = FEIED_MAP.getOrDefault(field, "joiningDate");
		Sort.Direction dir = "ASC".equalsIgnoreCase(order)?Sort.Direction.ASC : Sort.Direction.DESC;
		
		return Sort.by(dir,mappedfield);
	}
	
}
