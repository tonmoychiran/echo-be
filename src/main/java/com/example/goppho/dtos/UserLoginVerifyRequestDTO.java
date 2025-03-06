package com.example.goppho.dtos;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class UserLoginVerifyRequestDTO {
    @NotBlank(message = "OTP is empty")
    @Length(max = 6, message = "OTP must be 6 digits")
    String otp;

    @NotBlank(message = "Id is empty")
    String id;

    public String getOtp() {
        return otp;
    }

    public String getId() {
        return id;
    }
}
