package com.example.echo.responses;

import java.util.List;

public class DetailedErrorResponse<T> extends Response {
    List<T> errors;

    public DetailedErrorResponse(String message, List<T> errors) {
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
