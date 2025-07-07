package com.example.goppho.exceptions;

public class AuthorizationHeaderMissingException extends RuntimeException{
    public AuthorizationHeaderMissingException(String message) {
        super(message);
    }
}
