package com.example.echo.responses;

public class UserLoginOTPResponse extends Response {
    private String otpId;

    public UserLoginOTPResponse(String message, String otpId) {
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
