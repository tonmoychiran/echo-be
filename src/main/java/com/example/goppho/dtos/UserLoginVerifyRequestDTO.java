package com.example.goppho.dtos;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class UserLoginVerifyRequestDTO extends UserLoginRequestDTO {
    @NotBlank(message = "OTP is empty")
    @Length(max = 50, message = "OTP must be 6 digits")
    String otp;
}
