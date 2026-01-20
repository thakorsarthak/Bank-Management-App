package com.example.bankapp.Exception;

import java.util.List;

public class CustomValidationException extends RuntimeException{

	private final List<FieldError> errors;

	public CustomValidationException(String message,List<FieldError> errors) {

		 super(message);
		this.errors = errors;
	}

	public List<FieldError> getErrors() {
        return errors;
    }
}

