package com.example.bankapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDTO {

	private String account_hname;
	private Double balance;
	private String email;
	private String pin;
	private String confirmPin;
	private Long contact;
	private String account_type;

	/*
	 * public String getAccount_hname() { return account_hname; } public void
	 * setAccount_hname(String account_hname) { this.account_hname = account_hname;
	 * } public Double getBalance() { return balance; } public void
	 * setBalance(Double balance) { this.balance = balance; } public String
	 * getEmail() { return email; } public void setEmail(String email) { this.email
	 * = email; } public String getPin() { return pin; } public void setPin(String
	 * pin) { this.pin = pin; } public String getConfirmPin() { return confirmPin; }
	 * public void setConfirmPin(String confirmPin) { this.confirmPin = confirmPin;
	 * } public Long getContact() { return contact; } public void setContact(Long
	 * contact) { this.contact = contact; }
	 *
	 * public String getAccount_type() { return account_type; } public void
	 * setAccount_type(String account_type) { this.account_type = account_type; }
	 */
	// Getters and Setters
}
