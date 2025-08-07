package com.example.bankapp.DTO;

// Generates getters, setters, toString, equals, hashCode

public class TransactionReqDTO {

	private String accountNumber;
	private Double amount;
	private String pin;

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public TransactionReqDTO() {
		super();

	}

	public TransactionReqDTO(String accountNumber, Double amount, String pin) {

		this.accountNumber = accountNumber;
		this.amount = amount;
		this.pin = pin;
	}

	@Override
	public String toString() {
		return "TransactionReqDTO [accountNumber=" + accountNumber + ", amount=" + amount + ", pin=" + pin + "]";
	}

}
