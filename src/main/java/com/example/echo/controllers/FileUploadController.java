package com.example.echo.controllers;

import com.example.echo.annotations.ValidFile;
import com.example.echo.services.FileStorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/file")
public class FileUploadController {

    private final FileStorageService fileStorageService;

    @Autowired
    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("")
    public Boolean uploadFile(
            @Valid
            @ValidFile(
                    allowedTypes = {"image/jpeg", "image/png", "application/pdf"},
                    maxSize = 25 * 1024 * 1024,
                    message = "Invalid file(s)"
            )
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        this.fileStorageService.saveFile(file);

        return true;
    }
}
