package com.example.bankapp.Specification;


import org.springframework.data.jpa.domain.Specification;

import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.Employee;
import com.example.bankapp.enums.AccountStatus;
import jakarta.persistence.criteria.Join;

public class EmployeeSpecification {

	public static Specification<Employee> hasAccountStatus(AccountStatus accountStatus){
		
		return (root, query , cb) -> {
			if(accountStatus == null) {
				
				return cb.conjunction();
			}
			
			Join<Employee, Account> accountJoin	= root.join("account");
			
			return cb.equal(accountJoin.get("status"),accountStatus);
			
		};
	}
}
