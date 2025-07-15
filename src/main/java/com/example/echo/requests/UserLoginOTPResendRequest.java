package com.example.echo.requests;

import jakarta.validation.constraints.NotBlank;

public class UserLoginOTPResendRequest {
    @NotBlank(message = "OTP ID is empty")
    String otpId;

    public String getOtpId() {
        return otpId;
    }
}
