package com.example.goppho.dtos;

public class UserLoginRequestSuccessfulResponseDTO extends ResponseDTO {
    private String otpId;

    public UserLoginRequestSuccessfulResponseDTO(String message, String otpId) {
        super(message);
        this.otpId = otpId;
    }

    public String getOtpId() {
        return otpId;
    }

    public void setOtpId(String otpId) {
        this.otpId = otpId;
    }
}
