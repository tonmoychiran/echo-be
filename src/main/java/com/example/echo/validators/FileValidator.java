package com.example.echo.validators;

import com.example.echo.annotations.ValidFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {
    private List<String> allowedTypes;
    private long maxSize;

    @Override
    public void initialize(ValidFile constraintAnnotation) {
        this.allowedTypes = Arrays.asList(constraintAnnotation.allowedTypes());
        this.maxSize = constraintAnnotation.maxSize();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            context.buildConstraintViolationWithTemplate("File is empty")
                    .addConstraintViolation();
            return false;
        }

        if (!allowedTypes.isEmpty() && !allowedTypes.contains(file.getContentType())) {
            context.buildConstraintViolationWithTemplate("Allowed types: " + allowedTypes)
                    .addConstraintViolation();
            return false;
        }

        if (file.getSize() > maxSize) {
            context.buildConstraintViolationWithTemplate(
                            "File exceeds size limit of " + (maxSize / 1024 / 1024) + "MB")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}