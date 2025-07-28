package com.example.echo.validators;

import com.example.echo.annotations.ValidUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_.]+$";
    private static final Pattern PATTERN = Pattern.compile(USERNAME_REGEX);

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null) {
            return false;
        }
        Matcher matcher = PATTERN.matcher(username);
        return matcher.matches();
    }
}
