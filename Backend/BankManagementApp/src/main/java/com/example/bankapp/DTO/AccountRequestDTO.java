package com.example.bankapp.DTO;

import com.example.bankapp.entity.Address;

import jakarta.persistence.Column;

public class AccountRequestDTO {
	private String accountHolderName;
	private Double balance;
	private String email;
	private String password;
	private String confirmPassword;
	private String pin;
	private String confirmPin;
	private Long contact;
	private String panNumber;
	private String aadhaarNumber;
	private String accountType;
	private String branchCode;
	private String productCode;
	private Address address;

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getConfirmPin() {
		return confirmPin;
	}

	public void setConfirmPin(String confirmPin) {
		this.confirmPin = confirmPin;
	}

	public Long getContact() {
		return contact;
	}

	public void setContact(Long contact) {
		this.contact = contact;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getAadhaarNumber() {
		return aadhaarNumber;
	}

	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Override
	public String toString() {
		return "AccountRequestDTO [accountHolderName=" + accountHolderName + ", balance=" + balance + ", email=" + email
				+ ", password=" + password + ", confirmPassword=" + confirmPassword + ", pin=" + pin + ", confirmPin="
				+ confirmPin + ", contact=" + contact + ", panNumber=" + panNumber + ", aadhaarNumber=" + aadhaarNumber
				+ ", accountType=" + accountType + ", branchCode=" + branchCode + ", productCode=" + productCode
				+ ", address=" + address + "]";
	}
	
	
	
}
