package com.example.bankapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bankapp.entity.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Long>{

}
