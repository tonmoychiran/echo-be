package com.example.echo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Autowired
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(String key, InputStream inputStream, long contentLength) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.putObject(request, RequestBody.fromInputStream(inputStream, contentLength));
        return key;

    }

    public byte[] downloadFile(String key) {
        ResponseBytes<GetObjectResponse> objectAsBytes = s3Client.getObjectAsBytes(GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());
        return objectAsBytes.asByteArray();

    }
}
