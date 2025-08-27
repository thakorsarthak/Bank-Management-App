package com.example.bankapp.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.bankapp.config.CustomAcountDetails;
import com.example.bankapp.entity.Account;
import com.example.bankapp.repository.AccountRepo;
import com.example.bankapp.services.CustomAcountDetailService;



@Service
public class CustomAccountDetailServiceImp implements CustomAcountDetailService {

	@Autowired
	private AccountRepo accountRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Account account = accountRepo.findByEmail(email)
		        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

		    return new CustomAcountDetails(account);
	}

}
