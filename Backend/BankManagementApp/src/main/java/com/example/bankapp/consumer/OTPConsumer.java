package com.example.bankapp.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.bankapp.DTO.NotificationEvent;
import com.example.bankapp.config.RabbitMQConstants;
import com.example.bankapp.services.EmailService;
import com.example.bankapp.services.SmsService;

@Component
public class OTPConsumer {
	
	private final EmailService emailService;
	private final SmsService smsService;
	
	
	private OTPConsumer(EmailService emailService , SmsService smsService) {
		
		this.emailService = emailService;
		this.smsService = smsService;
	}
	
	// This listens to otp_queue
	@RabbitListener(queues = RabbitMQConstants.OTP_QUEUE)
	public void handleOtp(NotificationEvent event) {
		
		System.out.println("Consumer OTP recived" + event);
		
		 try {
	            if (event.getEmail() != null) {
	                emailService.sendOtpEmail(event.getEmail(), event.getOtp());
	            }

	            if (event.getPhone() != null) {
	                smsService.sendOtpSms(event.getPhone(), event.getOtp());
	            }

	            System.out.println(" OTP sent successfully!");

	        } catch (Exception ex) {
	            System.out.println("❌ Error sending OTP: " + ex.getMessage());
	            throw ex; // sending to DLQ automatically
	        }
	}

}
