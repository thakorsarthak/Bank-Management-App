package com.example.bankapp.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankapp.DTO.GlobalAPIResponseDTO;
import com.example.bankapp.repository.AccountRepo;
import com.example.bankapp.services.OtpService;



@RestController
@RequestMapping("/otp")
public class OtpController {

	@Autowired
	private OtpService otpService;

	@Autowired
	private AccountRepo accountRepo;

	@PostMapping("/send")
	public ResponseEntity<?> send(@RequestBody Map<String, String> req) {

		String email = req.get("email");
		String phone = req.get("phone");

		boolean accountExists = false;

		if (email != null && !email.trim().isEmpty()) {
			accountExists = accountRepo.existsByEmail(email);
		}

		if (!accountExists && phone != null && !phone.trim().isEmpty()) {
			try {

				String numericPhone = phone.replaceAll("[^0-9]", "");

				if (numericPhone.length() > 10 && numericPhone.startsWith("91")) {
					numericPhone = numericPhone.substring(2);
				}

				accountExists = accountRepo.existsByContact(numericPhone);
			} catch (NumberFormatException e) {
				return ResponseEntity.badRequest()
						.body(new GlobalAPIResponseDTO<>("Invalid phone number format",false));
			}
		}

		if (!accountExists) {
			return ResponseEntity.status(404)
					.body(new GlobalAPIResponseDTO<>("No account associated with this email or phone is present", false));
		}

		otpService.sendOtp(email, phone);

		return ResponseEntity.ok(new GlobalAPIResponseDTO<>("OTP sent Successfully",true));
	}

	@PostMapping("/verify")
	public ResponseEntity<?> verify(@RequestBody Map<String, String> req) {
		String email = req.get("email");
		String phone = req.get("phone");
		String otp = req.get("otp");

		boolean accountExists = false;

		if (email != null && !email.trim().isEmpty()) {
			accountExists = accountRepo.existsByEmail(email);
		}

		if (!accountExists && phone != null && !phone.trim().isEmpty()) {
			try {
				String numericPhone = phone.replaceAll("[^0-9]", "");

                if (numericPhone.length() > 10 && numericPhone.startsWith("91")) {
                    numericPhone = numericPhone.substring(2);
                }

				accountExists = accountRepo.existsByContact(numericPhone);
			} catch (NumberFormatException e) {
				return ResponseEntity.badRequest()
						.body(new GlobalAPIResponseDTO<>("Invalid phone number format" , false));
			}
		}

		if (!accountExists) {
			return ResponseEntity.status(404)
					.body(new GlobalAPIResponseDTO<>("No account associated with this email or phone", false));
		}

		boolean valid = otpService.verifyOtp(email, phone, otp);
		if (valid) {
			System.out.println("Correct OTP");
			return ResponseEntity.ok(Collections.singletonMap("message", "OTP verified"));
		} else {
			System.out.println("Incorrect OTP");
			return ResponseEntity.status(401).body(new GlobalAPIResponseDTO<>( "Invalid or expired OTP", false));
		}
	}
}
