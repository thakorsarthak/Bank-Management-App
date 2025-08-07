package com.example.bankapp.Exception;

import java.util.List;

public class CustomValidationException extends RuntimeException{

	private final List<FieldError> errors;

	public CustomValidationException(List<FieldError> errors) {

		super("validation failed");
		this.errors = errors;
	}

	public List<FieldError> getErrors() {
        return errors;
    }
}

