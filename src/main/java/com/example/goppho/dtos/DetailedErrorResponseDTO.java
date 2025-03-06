package com.example.goppho.dtos;

import java.util.List;

public class DetailedErrorResponseDTO<T> extends ResponseDTO {
    List<T> errors;

    public DetailedErrorResponseDTO(String message, List<T> errors) {
        super( message);
        this.errors = errors;
    }

    public List<T> getErrors() {
        return errors;
    }

    public void setErrors(List<T> errors) {
        this.errors = errors;
    }
}
