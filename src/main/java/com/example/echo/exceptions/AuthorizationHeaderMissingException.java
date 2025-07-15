package com.example.echo.exceptions;

public class AuthorizationHeaderMissingException extends RuntimeException{
    public AuthorizationHeaderMissingException(String message) {
        super(message);
    }
}
