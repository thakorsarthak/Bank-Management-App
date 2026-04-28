package com.example.bankapp.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.bankapp.entity.Branch;
import com.example.bankapp.repository.AccountRepo;
import com.example.bankapp.repository.BranchRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Order(1)
public class BranchBootstrap implements CommandLineRunner{
	
	private final BranchRepository branchRepository;
	
	@Override
	public void run(String... args) {
		
		createIfNotExists("1001", "Ahmedabad Branch", "BANK0AHM001", "Ahmedabad", "Gujarat");
        createIfNotExists("1002", "Surat Branch", "BANK0SUR001", "Surat", "Gujarat");
        createIfNotExists("1003", "Mumbai Branch", "BANK0MUM001", "Mumbai", "Maharashtra");
        createIfNotExists("1004", "Delhi Branch", "BANK0DEL001", "Delhi", "Delhi");
		
		
	}
	
	private void createIfNotExists(String code , String name , String ifsc , String city , String state) {
		
		boolean exists = branchRepository.existsByBranchCodeOrIfscCode(code , ifsc);
		
		if (!exists) {
			
			Branch branch = new Branch();
			 branch.setBranchCode(code);
	            branch.setBranchName(name);
	            branch.setIfscCode(ifsc);
	            branch.setCity(city);
	            branch.setState(state);
	            branch.setActive(true	);
	            
	            branchRepository.save(branch);
	            System.out.println("Created branch: " + name);
		}
	}

			
		
		
		
	
}
