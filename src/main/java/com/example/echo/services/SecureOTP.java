package com.example.echo.services;

import java.security.SecureRandom;

public class SecureOTP {
    public static String generateOTP(int length) {
        SecureRandom random = new SecureRandom();
        int upperBound = (int) Math.pow(10, length);
        int otp = random.nextInt(upperBound);
        return String.format("%0" + length + "d", otp);
    }
}
