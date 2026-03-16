package com.example.bankapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.bankapp.Filter.JwtFilter;

@Configuration
@EnableWebSecurity
public class securityConfig {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtFilter jwtFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


		return http
				 .cors(Customizer.withDefaults()) //to enable CORS

				.csrf(customizer -> customizer.disable())
				.authorizeHttpRequests(request -> request
						// Swagger UI - public
						.requestMatchers(
								"/webjars/**",
								"/swagger-ui/**",
								"/swagger-ui.html",
								"/v3/api-docs/**",
								"/swagger-resources/**")
						.permitAll()
 
						// Public auth endpoints
						.requestMatchers(
								"/main/create",
								"/main/login-account")
						.permitAll()
 
						// Public OTP endpoints (used before login for password/pin reset)
						.requestMatchers(
								"/otp/send",
								"/otp/verify")
						.permitAll()
						.requestMatchers(
								"/account/changePinWithOtp",
								"/account/changePasswordWithOtp")
						.permitAll()
 
						// Admin-only endpoints
						.requestMatchers("/admin/**")
						.hasRole("ADMIN")
						
						.anyRequest().authenticated())
				// .formLogin(Customizer.withDefaults())
			//	.httpBasic(Customizer.withDefaults())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}



	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		// provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
		provider.setUserDetailsService(userDetailsService);

		return provider;
	}



	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

}
