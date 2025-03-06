package com.example.goppho.dtos;

import java.util.List;

public class DetailedErrorResponseDTO<T> extends ResponseDTO {
    List<T> errors;

    public DetailedErrorResponseDTO(String message, List<T> errors) {
        super( message);
        this.errors = errors;
    }

    public List<T> getDetails() {
        return errors;
    }

    public void setDetails(List<T> errors) {
        this.errors = errors;
    }
}
