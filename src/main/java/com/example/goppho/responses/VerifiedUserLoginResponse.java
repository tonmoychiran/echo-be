package com.example.goppho.responses;

public class VerifiedUserLoginResponse extends Response {

    private String accessToken;
    private long expiresIn;

    public VerifiedUserLoginResponse(String message, String accessToken, long expiresIn) {
        super(message);
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }
}
