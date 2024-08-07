package com.dev.moim.domain.user.controller;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.user.UserService;
import com.dev.moim.domain.user.dto.CreateReviewDTO;
import com.dev.moim.domain.user.dto.ReviewListDTO;
import com.dev.moim.domain.user.dto.ProfileDetailDTO;
import com.dev.moim.domain.user.dto.ProfileListDTO;
import com.dev.moim.domain.user.dto.ProfileDTO;
import com.dev.moim.domain.user.dto.ProfileCreateDTO;
import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    private final UserService userService;

    @Operation(summary = "유저 기본 프로필 조회", description = "유저가 기본으로 설정한 프로필 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "유저 기본 프로필 조회 성공"),
    })
    @GetMapping("/profile")
    public BaseResponse<ProfileDTO> getProfile(
            @AuthUser User user
    ) {
        return null;
    }

    @Operation(summary = "프로필 생성", description = "유저의 프로필을 생성하는 기능입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON201", description = "프로필 생성 성공"),
    })
    @PostMapping("/profile")
    public BaseResponse<Long> createProfile(
            @RequestBody ProfileCreateDTO request
    ) {
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "프로필 수정", description = "유저의 프로필을 수정하는 기능입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON201", description = "프로필 수정 성공"),
    })
    @PutMapping("/profile")
    public BaseResponse<?> updateProfile(
            @RequestBody ProfileCreateDTO request
    ) {
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "프로필 목록 조회", description = "유저가 등록한 프로필들을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "프로필 목록 조회 성공"),
    })
    @GetMapping("/profile/list")
    public BaseResponse<ProfileListDTO> getProfileList() {

        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "프로필 상세 조회", description = "유저의 프로필을 상세 조회하는 기능입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "프로필 상세 조회 성공"),
    })
    @GetMapping(("/profile/{profileId}"))
    public BaseResponse<ProfileDetailDTO> getDetailProfile(
            @PathVariable Long profileId,
            @Parameter(description = "화면에 표시할 [받은 후기] 개수", example = "3") @RequestParam(defaultValue = "3") int receivedReviewCntLimit,
            @Parameter(description = "화면에 표시할 [참여 모임] 개수", example = "3") @RequestParam(defaultValue = "3") int joinedMoimCntLimit
    ) {

        return BaseResponse.onSuccess(null);
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
