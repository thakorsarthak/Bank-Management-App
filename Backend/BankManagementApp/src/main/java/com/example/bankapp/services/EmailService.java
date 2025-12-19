package com.example.bankapp.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private final JavaMailSender mailSender;

	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public void sendOtpEmail(String email, String otp) {
		SimpleMailMessage msg = new  SimpleMailMessage();
		msg.setTo(email);
		msg.setSubject("OTP Verification");
		msg.setText("Your OTP for reset password is : "+ otp);
		mailSender.send(msg);
		
	}
}
