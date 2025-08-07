package com.example.bankapp.DTO;

public class GlobalAPIResponseDTO<T> {

	private String message;
	private boolean success;
	private T data;

	public GlobalAPIResponseDTO() {

	}

	public GlobalAPIResponseDTO(String message, boolean success) {
		this.message = message;
		this.success = success;
	}

	public GlobalAPIResponseDTO(String message, boolean success, T data) {

		this.message = message;
		this.success = success;
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "GlobalAPIResponseDTO [message=" + message + ", success=" + success + ", data=" + data + "]";
	}

}
