package com.example.bankapp.Specification;


import org.springframework.data.jpa.domain.Specification;

import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.Branch;
import com.example.bankapp.entity.Employee;
import com.example.bankapp.enums.AccountStatus;
import com.example.bankapp.enums.Designation;
import com.example.bankapp.enums.Role;

import jakarta.persistence.criteria.Join;

public class AccountSpecification {

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

	public static Specification<Employee> hasbranch(Long branchId){

		return (root, query , cb) -> {
			if(branchId == null) {

				return cb.conjunction();
			}

			Join<Employee, Account> accountJoin	= root.join("account");
			Join<Account, Branch> branchJoin = accountJoin.join("branch");

			return cb.equal(branchJoin.get("id"),branchId);

		};
	}

	public static Specification<Account> hasUserStatus(AccountStatus status){

		return (root, query , cb) -> {
			if(status == null) {

				return cb.conjunction();
			}
			return cb.equal(root.get("status"),status);

		};
	}


	 public static Specification<Account> hasRole(Role role) {
	        return (root, query, cb) ->
	                cb.equal(root.get("role"), role);
	    }

	public static Specification<Account> hasbranchUser(Long branchId){

		return (root, query , cb) -> {
			if(branchId == null) {

				return cb.conjunction();
			}

			Join<Account, Branch> branchJoin = root.join("branch");

			return cb.equal(branchJoin.get("id"),branchId);

		};
	}


}
