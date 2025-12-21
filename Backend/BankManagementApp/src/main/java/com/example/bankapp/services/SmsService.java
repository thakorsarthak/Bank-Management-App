package com.example.bankapp.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SmsService {

	@Value("${twilio.phone.number}")
	private String twiliPhone;

	public void sendOtpSms(String phone, String otp) {

		Message.creator( new PhoneNumber(phone),
				new PhoneNumber(twiliPhone),
				"Your OTP is: "+ otp
				).create();

		 System.out.println(" SMS OTP Sent → " + phone);
	}

}
