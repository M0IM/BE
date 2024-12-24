package com.dev.moim.domain.moim.controller;

import com.dev.moim.domain.moim.dto.profile.MyUserMoimProfileDetailDTO;
import com.dev.moim.domain.moim.dto.profile.UpdateUserMoimProfileDTO;
import com.dev.moim.domain.moim.dto.profile.UserMoimProfileDetailDTO;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.service.UserMoimCommandService;
import com.dev.moim.domain.moim.service.UserMoimQueryService;
import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.security.annotation.annotation.AuthUserMoim;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/moims/{moimId}/profiles")
@Tag(name = "모임 프로필 관련 컨트롤러")
@RequiredArgsConstructor
@Validated
public class MoimProfileController {

    private final UserMoimQueryService userMoimQueryService;
    private final UserMoimCommandService userMoimCommandService;

    @Operation(summary = "자신의 모임 프로필 조회", description = "특정 모임 스페이스에서의 자신의 프로필을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    @GetMapping("")
    public BaseResponse<MyUserMoimProfileDetailDTO> getMyUserMoimProfile(
            @AuthUserMoim UserMoim userMoim,
            @PathVariable(name = "moimId") Long moimId) {
        return BaseResponse.onSuccess(userMoimQueryService.getMyUserMoimProfile(userMoim));
    }

    @Operation(summary = "모임 멤버 프로필 조회", description = "특정 모임 스페이스에서 특정 멤버의 모임 프로필을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    @GetMapping("{userMoimId}")
    public BaseResponse<UserMoimProfileDetailDTO> getUserMoimProfile(
            @AuthUserMoim UserMoim userMoim,
            @PathVariable(name = "moimId") Long moimId,
            @PathVariable(name = "userMoimId") Long userMoimId) {
        return BaseResponse.onSuccess(userMoimQueryService.getUserMoimProfile(moimId, userMoimId));
    }

    @Operation(summary = "자신의 모임 프로필 수정", description = "특정 모임 스페이스에서의 자신의 프로필을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    @PutMapping("")
    public BaseResponse<Long> updateUserMoimProfile(
            @AuthUserMoim UserMoim userMoim,
            @PathVariable(name = "moimId") Long moimId,
            @RequestBody UpdateUserMoimProfileDTO request) {
        return BaseResponse.onSuccess(userMoimCommandService.updateUserMoimProfile(userMoim, request));
    }
}
