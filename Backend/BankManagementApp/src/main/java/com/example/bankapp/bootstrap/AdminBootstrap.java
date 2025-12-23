package com.example.bankapp.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.bankapp.entity.Account;
import com.example.bankapp.enums.AccountStatus;
import com.example.bankapp.enums.Role;
import com.example.bankapp.repository.AccountRepo;

@Configuration
public class AdminBootstrap {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	CommandLineRunner initAdmin(AccountRepo accountRepo) {
		return args -> {

			if (!accountRepo.existsByEmail("admin@gmail.com")) {

				Account admin = new Account();
				admin.setEmail("admin@gmail.com");
				admin.setPassword(passwordEncoder.encode("Admin@1234"));
				admin.setRole(Role.ADMIN);
				admin.setStatus(AccountStatus.ACTIVE);
				admin.setBalance(0.0);

				accountRepo.save(admin);
				System.out.println(" Default admin created");
			}

		};
	}

}
