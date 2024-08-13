package com.dev.moim.domain.user.controller;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.user.service.UserService;
import com.dev.moim.domain.user.dto.CreateReviewDTO;
import com.dev.moim.domain.user.dto.ReviewListDTO;
import com.dev.moim.domain.user.dto.ProfileDetailDTO;
import com.dev.moim.domain.user.dto.ProfileDTO;
import com.dev.moim.domain.user.dto.ProfileCreateDTO;
import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.security.annotation.AuthUser;
import com.dev.moim.global.validation.annotation.ExistUserValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "유저 관련 컨트롤러")
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 기본 프로필 조회", description = "유저가 기본으로 설정한 프로필 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @GetMapping("/profile")
    public BaseResponse<ProfileDTO> getProfile(
            @AuthUser User user
    ) {
        return BaseResponse.onSuccess(userService.getProfile(user));
    }

    // TODO : 유저 프로필 수정
    @Operation(summary = "유저 프로필 수정", description = "유저의 프로필을 수정하는 기능입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON201", description = "요청 성공 및 리소스 생성됨"),
    })
    @PutMapping("/profile")
    public BaseResponse<?> updateProfile(
            @RequestBody ProfileCreateDTO request
    ) {
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "프로필 상세 조회", description = "유저의 프로필을 상세 조회하는 기능입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @GetMapping(("/profile/{userId}"))
    public BaseResponse<ProfileDetailDTO> getDetailProfile(
            @AuthUser User user,
            @ExistUserValidation @PathVariable Long userId
    ) {
        return BaseResponse.onSuccess(userService.getDetailProfile(userId));
    }

    @Operation(summary = "유저 후기 리스트 조회", description = "모임의 멤버가 유저에 대해 남긴 후기를 조회하는 기능입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @GetMapping(("/profile/reviews"))
    public BaseResponse<ReviewListDTO> getUserReviews(
            @AuthUser User user,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size
    ) {
        return BaseResponse.onSuccess(userService.getUserReviews(user, page, size));
    }

    @Operation(summary = "멤버 후기 작성 API", description = "멤버 후기 작성을 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/users/reviews")
    public BaseResponse<String> postMemberReview(@RequestBody CreateReviewDTO createReviewDTO) {
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
