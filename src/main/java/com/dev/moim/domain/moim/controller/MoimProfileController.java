package com.dev.moim.domain.moim.controller;

import com.dev.moim.domain.moim.dto.profile.GetUserMoimProfileDTO;
import com.dev.moim.domain.moim.dto.profile.UpdateUserMoimProfileDTO;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.service.MoimCommandService;
import com.dev.moim.domain.moim.service.MoimQueryService;
import com.dev.moim.domain.user.dto.UserPreviewListDTO;
import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.security.annotation.annotation.AuthUserMoim;
import com.dev.moim.global.validation.annotation.CheckCursorValidation;
import com.dev.moim.global.validation.annotation.CheckOwnerValidation;
import com.dev.moim.global.validation.annotation.CheckTakeValidation;
import com.dev.moim.global.validation.annotation.UserMoimValidaton;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/moims")
@Tag(name = "모임 프로필 관련 컨트롤러")
public class MoimProfileController {

    private final MoimQueryService moimQueryService;
    private final MoimCommandService moimCommandService;

    // 기존 API
    @Operation(summary = "모임 멤버 리스트 조회", description = "모임에 참여한 멤버들을 조회합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/{moimId}/members")
    public BaseResponse<UserPreviewListDTO> getMoimMembers(
            @AuthUserMoim UserMoim userMoim,
            @PathVariable @UserMoimValidaton Long moimId,
            @RequestParam(name = "cursor") Long cursor,
            @RequestParam(name = "take") Integer take,
            @RequestParam(name = "search") String search) {
        UserPreviewListDTO userPreviewListDTO = moimQueryService.getMoimMembers(moimId, cursor, take, search);
        return BaseResponse.onSuccess(userPreviewListDTO);
    }

    // 기존 API
    @Operation(summary = "모임 멤버 리스트 조회 (모임장 제외)", description = "모임장을 제외한 모임 멤버들을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/{moimId}/members/owner")
    public BaseResponse<UserPreviewListDTO> getMoimMembersExcludeOwner(
            @AuthUserMoim UserMoim userMoim,
            @CheckOwnerValidation @UserMoimValidaton @PathVariable Long moimId,
            @CheckCursorValidation @RequestParam(name = "cursor") Long cursor,
            @CheckTakeValidation @RequestParam(name = "take") Integer take,
            @RequestParam(name = "search") String search) {
        UserPreviewListDTO userPreviewListDTO = moimQueryService.getMoimMembersExcludeOwner(moimId, cursor, take, search);
        return BaseResponse.onSuccess(userPreviewListDTO);
    }

    // 추가된 API
    // TODO: 해당 모임에 접근 권한이 있는 유저인지 검증 추가 @AuthUserMoim 적용
    @Operation(summary = "특정 모임 멤버 프로필 상세 조회", description = "다른 멤버의 해당 모임에서의 프로필을 상세 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @GetMapping(("/{moimId}/members/{userMoimId}"))
    public BaseResponse<GetUserMoimProfileDTO> getUserMoimProfile(
            @PathVariable Long userMoimId
    ) {
        return BaseResponse.onSuccess(moimQueryService.getUserMoimProfile(userMoimId));
    }

    // 추가된 API
    // TODO: 해당 모임에 접근 권한이 있는 유저인지 검증 추가 @AuthUserMoim 적용
    @Operation(summary = "특정 모임 프로필 수정", description = "특정 모임에서의 프로필을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @PutMapping(("/{moimId}/profiles"))
    public BaseResponse<String> udateUserMoimProfile(
            UserMoim userMoim,
            @Valid @RequestBody UpdateUserMoimProfileDTO request
    ) {
        moimCommandService.udateUserMoimProfile(userMoim, request);
        return BaseResponse.onSuccess("모임 프로필이 수정되었습니다.");
    }
}
