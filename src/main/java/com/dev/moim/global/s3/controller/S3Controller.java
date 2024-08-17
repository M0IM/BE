package com.dev.moim.global.s3.controller;


import com.dev.moim.global.common.BaseResponse;

import com.dev.moim.global.s3.dto.AwsDTO.*;
import com.dev.moim.global.s3.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/s3")
public class S3Controller {

    private final S3Service s3Service;

    @Operation(summary = "Upload용 Presigned URL 생성", description = "업로드를 위한 Presigned URL을 생성한다")
    @PostMapping("/presigned/upload")
    public BaseResponse<PresignedUrlUploadResponse> getPresignedUrlToUpload(@RequestBody PresignedUploadRequest presignedUploadRequest) {
        PresignedUrlUploadResponse presignedUrlToUpload = s3Service.getPresignedUrlToUpload(presignedUploadRequest);
        return BaseResponse.onSuccess(presignedUrlToUpload);
    }

    @Operation(summary = "여러 개 Upload용 Presigned URL 생성", description = "업로드를 위한 Presigned URL를 여러 개 생성한다")
    @PostMapping("/presigned/upload/list")
    public BaseResponse<?> getPresignedUrlToUploadList(@RequestBody PresignedUploadListRequest presignedUploadListRequest) {
        PresignedUrlUploadResponseList presignedUrlToUploadList = s3Service.getPresignedUrlToUploadList(presignedUploadListRequest);
        return BaseResponse.onSuccess(presignedUrlToUploadList);
    }
}
