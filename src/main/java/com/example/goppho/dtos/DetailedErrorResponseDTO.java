package com.example.goppho.dtos;

import java.util.List;

public class DetailedErrorResponseDTO<T> extends ErrorResponseDTO {
    List<T> details;

    public DetailedErrorResponseDTO(String message, List<T> details) {
        super( message);
        this.details = details;
    }

    public List<T> getDetails() {
        return details;
    }

    public void setDetails(List<T> details) {
        this.details = details;
    }
}
