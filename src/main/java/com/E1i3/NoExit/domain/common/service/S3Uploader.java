package com.E1i3.NoExit.domain.common.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
public class S3Uploader {

    private final AmazonS3 amazonS3;
    private final String bucket;

    @Autowired
    public S3Uploader(AmazonS3 amazonS3, @Value("${AWS_BUCKET_NAME}") String bucket) {
        this.amazonS3 = amazonS3;
        this.bucket = bucket;
    }

    public String upload(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, fileName).toString();
    }
}