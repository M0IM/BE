package com.dev.moim.global.s3.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.dev.moim.global.s3.dto.AwsDTO;
import com.dev.moim.global.s3.dto.AwsDTO.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public PresignedUrlUploadResponse getPresignedUrlToUpload(PresignedUploadRequest presignedUploadRequest) {
        /// 제한시간 설정
        Date expiration = new Date();
        long expTime = expiration.getTime();
        expTime += TimeUnit.MINUTES.toMillis(3); // 3 Minute
        expiration.setTime(expTime);

        String keyName = UUID.randomUUID() + "_" + presignedUploadRequest.getKeyName();

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, keyName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);

        String key = generatePresignedUrlRequest.getKey();

        return PresignedUrlUploadResponse.builder()
                .url(amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString())
                .keyName(key)
                .build();

    }

    public PresignedUrlDownLoadResponse getPresignedUrlToDownload(PresignedDownloadRequest presignedDownloadRequest) {
        /// 제한시간 설정
        Date expiration = new Date();
        long expTime = expiration.getTime();
        expTime += TimeUnit.MINUTES.toMillis(3);
        expiration.setTime(expTime); // 3 Minute

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, presignedDownloadRequest.getKeyName())
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);

        return PresignedUrlDownLoadResponse.builder()
                .url(amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString())
                .build();
    }

    public Boolean isExistKeyName(String keyName) {
        return amazonS3.doesObjectExist(bucket, keyName);
    }



    public PresignedUrlUploadResponseList getPresignedUrlToUploadList(PresignedUploadListRequest presignedUploadListRequest) {
        // Set expiration time for 3 minutes from now
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + TimeUnit.MINUTES.toMillis(3));

        System.out.println(1);

        // Generate the list of presigned URLs
        List<PresignedUrlUploadResponse> responses = presignedUploadListRequest.getKeyNames().stream()
                .map(oldKeyName -> {
                    String keyName = UUID.randomUUID() + "_" + oldKeyName;

                    GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, keyName)
                            .withMethod(HttpMethod.PUT)
                            .withExpiration(expiration);

                    String url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();

                    return PresignedUrlUploadResponse.builder()
                            .url(url)
                            .keyName(keyName)
                            .build();
                })
                .collect(Collectors.toList());

        System.out.println(2);
        // Return the response list
        return PresignedUrlUploadResponseList.builder().presignedUrlUploadResponses(responses).build();
    }

    public PresignedUrlDownLoadResponseList getPresignedUrlToDownloadList(PresignedDownLoadListRequest downloadListPresigned) {
        // Set expiration time for 3 minutes from now
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + TimeUnit.MINUTES.toMillis(3));

        // Generate the list of presigned URLs for downloading
        List<PresignedUrlDownLoadResponse> responses = downloadListPresigned.getKeyNames().stream()
                .map(keyName -> {

                    GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, keyName)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration);

                    // Generate the presigned URL
                    String url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();

                    // Build the response
                    return PresignedUrlDownLoadResponse.builder()
                            .url(url)
                            .build();
                })
                .collect(Collectors.toList());

        // Return the response list
        return PresignedUrlDownLoadResponseList.builder().presignedUrlDownLoadResponseLists(responses).build();
    }
}