package com.example.bankapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bankapp.entity.OtpRecord;


public interface OtpRepository extends JpaRepository<OtpRecord, Long> {

	Optional<OtpRecord> findByEmailAndOtp(String email, String Otp);
	Optional<OtpRecord> findByPhoneAndOtp(String phone, String Otp);
}
