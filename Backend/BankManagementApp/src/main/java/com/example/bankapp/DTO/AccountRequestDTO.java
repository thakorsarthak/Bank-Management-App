package com.example.bankapp.DTO;

public class AccountRequestDTO {
	private String accountHolderName;
	private Double balance;
	private String email;
	private String pin;
	private String confirmPin;
	private Long contact;
	private String accountType;
	private String branchCode;
	private String productCode;

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
	@Override
	public String toString() {
		return "AccountRequestDTO [accountHolderName=" + accountHolderName + ", balance=" + balance + ", email=" + email
				+ ", pin=" + pin + ", confirmPin=" + confirmPin + ", contact=" + contact + ", accountType="
				+ accountType + ", branchCode=" + branchCode + ", productCode=" + productCode + "]";
	}



}
