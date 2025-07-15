package com.example.echo.security;

import com.example.echo.entities.UserAuthOTPEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class OTPAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;

    public OTPAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String email = authentication.getName();
        String otp = authentication.getCredentials().toString();

        UserAuthOTPEntity userDetails = (UserAuthOTPEntity) userDetailsService.loadUserByUsername(email);

        // Validate OTP and expiration
        if (!userDetails.getOtp().equals(otp)) {
            throw new BadCredentialsException("Invalid OTP");
        }
        if (!userDetails.isCredentialsNonExpired()) {
            throw new BadCredentialsException("OTP has expired");
        }

        // Return authenticated token
        return new UsernamePasswordAuthenticationToken(email, otp, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
