package com.example.echo.services;


import com.example.echo.entities.AttachmentEntity;
import com.example.echo.repositories.AttachmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;

@Service
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final S3Service s3Service;

    @Autowired
    public AttachmentService(
            AttachmentRepository attachmentRepository,
            S3Service s3Service
    ) {
        this.attachmentRepository = attachmentRepository;
        this.s3Service = s3Service;
    }

    private static final String STORAGE_DIRECTORY = "src/main/resources/static/";

    @Transactional
    public AttachmentEntity saveFile(MultipartFile file) throws IOException {

        String fileName = Instant.now().toEpochMilli() + file.getOriginalFilename();
        AttachmentEntity attachment = this.attachmentRepository.save(new AttachmentEntity(fileName));

        this.s3Service.uploadFile(fileName, file.getInputStream(), file.getSize());

        return attachment;
    }

    public byte[] getFile(String key) {
        return this.s3Service.downloadFile(key);
    }
}
