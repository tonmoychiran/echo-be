package com.example.echo.controllers;

import com.example.echo.annotations.ValidFile;
import com.example.echo.services.S3Service;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/v1/file")
public class FileUploadController {

    private final S3Service s3Service;

    @Autowired
    public FileUploadController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/save")
    public String uploadFile(
            @Valid
            @ValidFile(
                    allowedTypes = {"image/jpeg", "image/png", "application/pdf"},
                    maxSize = 25 * 1024 * 1024,
                    message = "Invalid file(s)"
            )
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        return this.s3Service.uploadFile(file.getOriginalFilename(), file.getInputStream(), file.getSize());
    }

    @GetMapping("/{key}")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable("key") String key
    ) {
        byte[] data = this.s3Service.downloadFile(key);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + key)
                .body(data);
    }
}
