package com.example.goppho.dtos;

public class ValidationErrorDTO {
    String field;
    String issue;

    public ValidationErrorDTO(String field, String issue) {
        this.field = field;
        this.issue = issue;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }
}
