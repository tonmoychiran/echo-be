package com.example.goppho.configs;


import com.example.goppho.responses.DetailedErrorResponse;
import com.example.goppho.responses.Response;
import com.example.goppho.responses.ValidationErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.List;


@ControllerAdvice
public class GlobalExceptionHandler {

    //validation exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationException(
            MethodArgumentNotValidException ex
    ) {
        List<FieldError> fieldErrors = ex.getFieldErrors();
        List<ValidationErrorResponse> validationErrors = new ArrayList<>();

        for (FieldError fieldError : fieldErrors) {
            validationErrors.add(
                    new ValidationErrorResponse(
                            fieldError.getField(),
                            fieldError.getDefaultMessage()
                    ));
        }

        return new ResponseEntity<>(
                new DetailedErrorResponse<>(
                        "Validation Error",
                        validationErrors
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    //data exceptions
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Response> handleEntityNotFoundExceptionException(
            Exception ex
    ) {
        return new ResponseEntity<>(
                new Response(ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<Response> handleCredentialsExpiredException(
            Exception ex
    ) {
        return new ResponseEntity<>(
                new Response(ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Response> handleBadCredentialsException(
            Exception ex
    ) {
        return new ResponseEntity<>(
                new Response(ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }


    //http exceptions
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Response> handleNoResourceFoundException(
            Exception ex
    ) {
        return new ResponseEntity<>(
                new Response(ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Response> handleHttpRequestMethodNotSupportedException(
            Exception ex
    ) {
        return new ResponseEntity<>(
                new Response(ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    //general exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleGeneralException(
            Exception ex
    ) {
        System.out.println(ex.getClass());
        System.out.println(ex.getMessage());
        return new ResponseEntity<>(
                new Response(ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
