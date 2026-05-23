package com.example.bankapp.Exception;

import java.time.LocalDateTime;
import java.util.List;



public class ApiError {

	private int status;
    private String message;
    private LocalDateTime timestamp;
    private List<FieldError> errors;
	public ApiError(int status, String message, List<FieldError> errors) {

		this.status = status;
		this.message = message;
		this.timestamp = LocalDateTime.now();
		this.errors = errors;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public List<FieldError> getErrors() {
		return errors;
	}
	public void setErrors(List<FieldError> errors) {
		this.errors = errors;
	}
}
