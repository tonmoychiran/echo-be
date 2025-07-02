package com.example.goppho.requests;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class UserLoginVerificationRequest extends UserLoginOTPRequest {
    @NotBlank(message = "OTP is empty")
    @Length(max = 6, message = "OTP must be 6 digits")
    String otp;

    public String getOtp() {
        return otp;
    }
}
