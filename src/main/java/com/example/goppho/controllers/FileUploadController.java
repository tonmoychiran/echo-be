package com.example.goppho.controllers;

import com.example.goppho.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/file")
public class FileUploadController {

    private FileStorageService fileStorageService;

    @Autowired
    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload-file")
    public Boolean uploadFile(
            @RequestParam("files") MultipartFile[] files
    ) throws IOException {
        for(MultipartFile file : files) {
            fileStorageService.saveFile(file);
        }

        return true;
    }
}
