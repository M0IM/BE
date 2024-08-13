package com.dev.moim.global.s3.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AwsDTO {

    @Schema(description = "AWS S3 URL 응답 정보")
    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PresignedUrlUploadResponse {
        private String url;
        private String keyName;
    }

    @Schema(description = "AWS S3 URL 응답 정보")
    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PresignedUrlDownLoadResponse {
        private String url;
    }
}
