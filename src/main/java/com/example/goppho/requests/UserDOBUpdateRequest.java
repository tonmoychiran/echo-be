package com.example.goppho.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class UserDOBUpdateRequest {
    @NotNull(message="Date of Birth is empty")
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    @Past(message = "Invalid Date Of Birth")
    LocalDate dob;

    public LocalDate getDob() {
        return dob;
    }
}
