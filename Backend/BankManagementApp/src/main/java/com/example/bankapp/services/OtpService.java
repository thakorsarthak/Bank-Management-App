package com.example.bankapp.services;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.bankapp.entity.OtpRecord;
import com.example.bankapp.repository.OtpRepository;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class OtpService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private OtpRepository otpRepo;

	@Value("${twilio.phone.number}")
	private String twiliPhone;

	private String generateOtp() {

		return String.format("%06d", new SecureRandom().nextInt(999999));
	}

	public void sendOtp(String email, String phone) {

		String otp = generateOtp();
		LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);
		System.out.println("Sending otp");
		OtpRecord record = new OtpRecord();

		record.setEmail(email);
		record.setPhone(phone);
		record.setOtp(otp);
		record.setExpiryTime(expiry);

		otpRepo.save(record);

		if (email != null) {
			sendEmail(email, otp);
		}
		if (phone != null) {
			sendSms(phone, otp);
		}

	}

	public boolean verifyOtp(String email, String phone, String otp) {
		System.out.println("Inside Verify OTP");
		Optional<OtpRecord> result = (email != null)
				? otpRepo.findByEmailAndOtp(email, otp) : otpRepo.findByPhoneAndOtp(phone, otp);
		return result.isPresent() && result.get().getExpiryTime().isAfter(LocalDateTime.now());
	}


	private void sendEmail(String email, String otp) {

		SimpleMailMessage msg = new  SimpleMailMessage();
		msg.setTo(email);
		msg.setSubject("OTP Verification");
		msg.setText("Your OTP is: "+ otp);
		mailSender.send(msg);
	}

	private void sendSms(String phone, String otp) {

		Message.creator( new PhoneNumber(phone),
				new PhoneNumber(twiliPhone),
				"Your OTP is: "+ otp
				).create();
	}
}
