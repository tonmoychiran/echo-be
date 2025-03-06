package com.example.goppho.configs;


import com.example.goppho.dtos.DetailedErrorResponseDTO;
import com.example.goppho.dtos.ResponseDTO;
import com.example.goppho.dtos.ValidationErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO> handleValidationException(
            MethodArgumentNotValidException ex
    ) {
        List<FieldError> fieldErrors = ex.getFieldErrors();
        List<ValidationErrorDTO> validationErrors = new ArrayList<>();

        for (FieldError fieldError : fieldErrors) {
            validationErrors.add(
                    new ValidationErrorDTO(
                            fieldError.getField(),
                            fieldError.getDefaultMessage()
                    ));
        }

        return new ResponseEntity<>(
                new DetailedErrorResponseDTO<>(
                        "Validation Error",
                        validationErrors
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ResponseDTO> handleNoResourceFoundException(
            Exception ex
    ) {
        return new ResponseEntity<>(
                new ResponseDTO(ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseDTO> handleHttpRequestMethodNotSupportedExceptionException(
            Exception ex
    ) {
        return new ResponseEntity<>(
                new ResponseDTO(ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleGeneralException(
            Exception ex
    ) {
        System.out.println(ex.getClass());
        System.out.println(ex.getMessage());
        return new ResponseEntity<>(
                new ResponseDTO(ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
