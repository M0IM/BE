package com.dev.moim.domain.user.controller;

import com.dev.moim.domain.user.dto.CreateReviewDTO;
import com.dev.moim.domain.user.dto.ReviewListDTO;
import com.dev.moim.domain.user.dto.ProfileDetailGetResponse;
import com.dev.moim.domain.user.dto.ProfileListGetResponse;
import com.dev.moim.domain.user.dto.ProfileResponse;
import com.dev.moim.domain.user.dto.ProfileUpdateRequest;
import com.dev.moim.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "유저 관련 컨트롤러")
public class UserController {

    @GetMapping("")
    @Operation(summary = "마이페이지 조회", description = "마이페이지 메인 화면 조회 기능 입니다.")
    public BaseResponse<ProfileResponse> getProfile() {

        return BaseResponse.onSuccess(null);
    }

    @GetMapping("/list")
    @Operation(summary = "(마이페이지 : 내 프로필 관리) 내 프로필 목록 조회", description = "유저의 프로필들을 조회할 수 있는 <내 프로필 목록> 조회 기능 입니다.")
    public BaseResponse<ProfileListGetResponse> getProfileList() {

        return BaseResponse.onSuccess(null);
    }

    @GetMapping("/detail")
    @Operation(summary = "(마이페이지 : 내 프로필 관리 : 내 프로필 목록 조회) 내 프로필 상세 조회",
            description = "유저의 특정 프로필을 상세 조회하는 기능 입니다. '모임 평가 별점'과 '가입 모임'은 유저의 모든 프로필에서 동일하게 표시되어 프로필 별 설정이 불가능 합니다.")
    public BaseResponse<ProfileDetailGetResponse> getDetailProfile() {

        return BaseResponse.onSuccess(null);
    }

    @PostMapping("/update")
    @Operation(summary = "내 프로필 수정", description = "유저의 프로필을 수정하는 기능 입니다.")
    public BaseResponse<?> updateProfile(
            @RequestBody ProfileUpdateRequest request
    ) {
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "멤버 후기 작성 API", description = "멤버 후기 작성을 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/users/reviews")
    public BaseResponse<String> postMemberReview(@io.swagger.v3.oas.annotations.parameters.RequestBody CreateReviewDTO createReviewDTO) {
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "멤버 후기 상세보기 API", description = "멤버 후기를 조회 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/users/{userId}/reviews")
    public BaseResponse<ReviewListDTO> getMemberReview(@PathVariable Long userId) {
        return BaseResponse.onSuccess(null);
    }
}
