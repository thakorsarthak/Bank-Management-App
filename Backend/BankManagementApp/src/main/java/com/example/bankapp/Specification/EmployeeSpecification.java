package com.example.bankapp.Specification;


import org.springframework.data.jpa.domain.Specification;

import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.Employee;
import com.example.bankapp.enums.AccountStatus;
import com.example.bankapp.enums.Designation;

import jakarta.persistence.criteria.Join;

public class EmployeeSpecification {

	public static Specification<Employee> hasAccountStatus(AccountStatus status){

		return (root, query , cb) -> {
			if(status == null) {

				return cb.conjunction();
			}

			Join<Employee, Account> accountJoin	= root.join("account");

			return cb.equal(accountJoin.get("status"),status);

		};
	}

	public static Specification<Employee> hasDesignation(Designation designation) {

	    return (root, query, cb) -> 

//	        if (designation == null) {
//	            return cb.conjunction();
//	            
//	        }
//
//	        return cb.equal(root.get("designation"), designation);
	    	designation == null ? null : cb.equal(root.get("designation"), designation);
	    
	}

}
