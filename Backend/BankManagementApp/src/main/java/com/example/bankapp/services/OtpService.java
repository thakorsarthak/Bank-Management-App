package com.example.bankapp.services;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.example.bankapp.DTO.NotificationEvent;
import com.example.bankapp.config.RabbitMQConstants;
import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.OtpRecord;
import com.example.bankapp.repository.AccountRepo;
import com.example.bankapp.repository.OtpRepository;

@Service
public class OtpService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private OtpRepository otpRepo;

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private AccountRepo accountRepo;
	
	@Autowired
	private RateLimitService rateLimitService;

//	@Value("${twilio.phone.number}")
//	private String twiliPhone;

	private String generateOtp() {

		return String.format("%06d", new SecureRandom().nextInt(999999));
	}

	public void sendOtp(String email, String phone) {
		
		Optional<Account> account  = accountRepo.findByEmail(email);
		
		Account acc = account.get();
		
		String accountId = acc.getEmail();
		
		String key =
		        "otp:" + accountId;

		boolean allowed =
		        rateLimitService.isAllowed(
		                key,
		                3,
		                Duration.ofMinutes(5)
		        );

		if (!allowed) {

		    throw new BadCredentialsException(
		            "Too many OTP requests"
		    );
		}

		String otp = generateOtp();
		LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);
		System.out.println("inside Sending otp service");
		OtpRecord record = new OtpRecord();

		record.setEmail(email);
		record.setPhone(phone);
		record.setOtp(otp);
		record.setExpiryTime(expiry);

		otpRepo.save(record);

//		if (email != null) {
//			sendEmail(email, otp);
//		}
//		if (phone != null) {
//			sendSms(phone, otp);
//		}

		 NotificationEvent event = new NotificationEvent();
		    event.setEmail(email);
		    event.setPhone(phone);
		    event.setMessageType("OTP");
		    event.setOtp(otp);

		    // Publish to RabbitMQ
		    rabbitTemplate.convertAndSend(
		        RabbitMQConstants.NOTIFICATION_EXCHANGE,
		        RabbitMQConstants.OTP_ROUTING_KEY,
		        event
		    );

		    System.out.println("OTP published to RabbitMQ: " + otp);

	}

//	public boolean verifyOtp(String email, String phone, String otp) {
//		System.out.println("Inside Verify OTP");
//		Optional<OtpRecord> result = (email != null)
//				? otpRepo.findByEmailAndOtp(email, otp) : otpRepo.findByPhoneAndOtp(phone, otp);
//		return result.isPresent() && result.get().getExpiryTime().isAfter(LocalDateTime.now());
//	}


	 public boolean verifyOtp(String email, String phone, String otp) {

	        Optional<OtpRecord> result = (email != null)
	                ? otpRepo.findByEmailAndOtp(email, otp)
	                : otpRepo.findByPhoneAndOtp(phone, otp);

	        if (result.isEmpty()) {
				return false;
			}

	        OtpRecord record = result.get();

	        // expiry check
	        if (record.getExpiryTime().isBefore(LocalDateTime.now())) {
	            return false;
	        }

	        // OTP matched and valid → delete it
	        otpRepo.delete(record);

	        return true;
	    }

//	private void sendEmail(String email, String otp) {
//
//		SimpleMailMessage msg = new  SimpleMailMessage();
//		msg.setTo(email);
//		msg.setSubject("OTP Verification");
//		msg.setText("Your OTP is: "+ otp);
//		mailSender.send(msg);
//	}
//
//	private void sendSms(String phone, String otp) {
//
//		Message.creator( new PhoneNumber(phone),
//				new PhoneNumber(twiliPhone),
//				"Your OTP is: "+ otp
//				).create();
//	}
}
