package com.example.bankapp.Exception;

public class RateLimitExceededException  extends RuntimeException {

    public RateLimitExceededException(
            String message
    ) {
        super(message);
    }
}