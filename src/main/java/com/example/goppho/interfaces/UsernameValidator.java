package com.example.goppho.interfaces;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = com.example.goppho.validators.UsernameValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameValidator {
    String message() default "Username only allows numbers, letters, underscores (_), periods (.))";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}