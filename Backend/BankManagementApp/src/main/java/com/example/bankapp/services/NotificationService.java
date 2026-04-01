package com.example.bankapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.bankapp.DTO.NotificationRequestDTO;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class NotificationService {

	@Autowired
	private JavaMailSender mailSender;

	@Value("${twilio.account.sid}")
	private String twilioSid;

	@Value("${twilio.auth.token}")
	private String twilioAuthToken;

	@Value("${twilio.phone.number}")
	private String twilioPhone;


	@Async
	public void sendTransactionNotification(NotificationRequestDTO notification) {

		if (notification.getEmail() != null && !notification.getEmail().isBlank()) {
			sendEmail(notification.getEmail(), notification.getSubject(), notification.getMessage());
		}
		if (notification.getPhone() != null && !notification.getPhone().isBlank()) {

			sendSms(notification.getPhone(), notification.getMessage());
		}
	}

	private void sendEmail(String toEmail, String subject, String body) {
		try {
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(toEmail);
			msg.setSubject(subject);
			msg.setText(body);
			mailSender.send(msg);

		} catch (Exception e) {
			// TODO: handle exception
			  System.err.println("Email sending failed: " + e.getMessage());
		}
	}

	private void sendSms(String toPhone, String body) {

		try {
			Message.creator(
					new PhoneNumber(toPhone), new PhoneNumber(twilioPhone), body).create();
		} catch (Exception e) {
			// TODO: handle exception
			 System.err.println("SMS sending failed: " + e.getMessage());
		}
	}

	private String buildMessage(NotificationRequestDTO dto) {
        StringBuilder sb = new StringBuilder();
        sb.append("Transaction Status: ").append(dto.getStatus()).append("\n");
        sb.append("Amount: ₹").append(dto.getAmount()).append("\n");
        sb.append("Sender Account: ").append(dto.getSenderAccountNo()).append("\n");
        sb.append("Receiver Account: ").append(dto.getReceiverAccountNo()).append("\n");
        sb.append("Time: ").append(dto.getTransactionTime()).append("\n");
        sb.append("Thank you for banking with us!");
        return sb.toString();
    }
}
