package com.example.goppho.dtos;

public class UserLoginRequestSuccessfulResponseDTO extends ResponseDTO {

    private final String otpId;

    public UserLoginRequestSuccessfulResponseDTO(String message, String otpId) {
        super(message);
        this.otpId = otpId;
    }

}
