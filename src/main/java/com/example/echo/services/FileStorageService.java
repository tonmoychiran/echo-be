package com.example.echo.services;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;

@Service
public class FileStorageService {

    private static final String STORAGE_DIRECTORY = "src/main/resources/static/";

    public boolean saveFile(MultipartFile file) throws IOException {
        File tragetFile = new File(
                STORAGE_DIRECTORY +
                        File.separator +
                        Instant.now().toEpochMilli() +
                        file.getOriginalFilename()
        );
        Files.copy(file.getInputStream(), tragetFile.toPath());
        return true;
    }
}
