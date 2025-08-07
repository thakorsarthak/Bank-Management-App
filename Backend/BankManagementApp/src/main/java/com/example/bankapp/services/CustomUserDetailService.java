package com.example.bankapp.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public interface CustomUserDetailService extends UserDetailsService {

	@Override
	UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

}
