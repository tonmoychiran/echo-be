package com.example.goppho.responses;

public class ValidationErrorResponse {
    String field;
    String issue;

    public ValidationErrorResponse(String field, String issue) {
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
