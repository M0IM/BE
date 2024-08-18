package com.dev.moim.domain.user.controller;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.calender.UserDailyPlanPageDTO;
import com.dev.moim.domain.user.dto.*;
import com.dev.moim.domain.user.service.UserCommandService;
import com.dev.moim.domain.user.service.UserQueryService;
import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.security.annotation.AuthUser;
import com.dev.moim.global.validation.annotation.CheckPageValidation;
import com.dev.moim.global.validation.annotation.CheckSizeValidation;
import com.dev.moim.global.validation.annotation.ExistUserValidation;
import com.dev.moim.global.validation.annotation.IndividualPlanValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/v1/users")
@Tag(name = "유저 관련 컨트롤러")
public class UserController {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @Operation(summary = "유저 기본 프로필 조회", description = "유저가 기본으로 설정한 프로필 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @GetMapping("/profile")
    public BaseResponse<ProfileDTO> getProfile(
            @AuthUser User user
    ) {
        return BaseResponse.onSuccess(userQueryService.getProfile(user));
    }

    @Operation(summary = "유저 프로필 수정", description = "유저의 프로필을 수정하는 기능입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON201", description = "요청 성공 및 리소스 생성됨"),
    })
    @PutMapping("/profile")
    public BaseResponse<Void> updateProfile(
            @AuthUser User user,
            @Valid @RequestBody UpdateUserInfoDTO request
    ) {
        userCommandService.updateInfo(user, request);
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "유저 프로필 상세 조회", description = "유저의 프로필을 상세 조회하는 기능입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @GetMapping(("/profile/detail"))
    public BaseResponse<ProfileDetailDTO> getUserDetailProfile(
            @AuthUser User user
    ) {
        return BaseResponse.onSuccess(userQueryService.getDetailProfile(user.getId()));
    }

    @Operation(summary = "멤버 프로필 상세 조회", description = "다른 멤버의 프로필을 상세 조회하는 기능입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @GetMapping(("/profile/detail/{userId}"))
    public BaseResponse<ProfileDetailDTO> getMemberDetailProfile(
            @ExistUserValidation @PathVariable Long userId
    ) {
        return BaseResponse.onSuccess(userQueryService.getDetailProfile(userId));
    }

    @Operation(summary = "유저 후기 리스트 조회", description = "유저 자신의 후기를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @GetMapping(("/reviews"))
    public BaseResponse<ReviewListDTO> getUserReviews(
            @AuthUser User user,
            @CheckPageValidation @RequestParam(name = "page") int page,
            @CheckSizeValidation @RequestParam(name = "size") int size
    ) {
        return BaseResponse.onSuccess(userQueryService.getUserReviews(user.getId(), page, size));
    }

    @Operation(summary = "멤버 후기 리스트 조회", description = "다른 유저의 후기를 조회합니다. 조회할 멤버의 id를 넣어주세요.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @GetMapping(("/reviews/{userId}"))
    public BaseResponse<ReviewListDTO> getMemberReviews(
            @ExistUserValidation @PathVariable Long userId,
            @CheckPageValidation @RequestParam(name = "page") int page,
            @CheckSizeValidation @RequestParam(name = "size") int size
    ) {
        return BaseResponse.onSuccess(userQueryService.getUserReviews(userId, page, size));
    }

    @Operation(summary = "멤버 후기 작성", description = "멤버 후기 작성을 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @PostMapping("/reviews")
    public BaseResponse<CreateReviewResultDTO> postMemberReview(
            @AuthUser User user,
            @RequestBody CreateReviewDTO createReviewDTO) {
        return BaseResponse.onSuccess(userCommandService.postMemberReview(user, createReviewDTO));
    }

    @Operation(summary = "push 알림 설정", description = "push 알림이 켜져있으면 끄고 꺼져있으면 킵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @PostMapping("/alarms/push")
    public BaseResponse<AlarmDTO> settingPushAlarm(@AuthUser User user) {
        return BaseResponse.onSuccess(userCommandService.settingPushAlarm(user));
    }

    @Operation(summary = "event 알림 설정", description = "event 알림이 켜져있으면 끄고 꺼져있으면 킵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @PostMapping("/alarms/event")
    public BaseResponse<AlarmDTO> settingEventAlarm(@AuthUser User user) {
        return BaseResponse.onSuccess(userCommandService.settingEventAlarm(user));
    }

    @Operation(summary = "알림 상태 조회", description = "알림 상태를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @GetMapping("/alarms")
    public BaseResponse<AlarmDTO> getAlarmSetting(@AuthUser User user) {
        return BaseResponse.onSuccess(AlarmDTO.toAlarmDTO(user));
    }

    @Operation(summary = "개인 일정 추가", description = "개인의 일정을 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON201", description = "요청 성공 및 리소스 생성됨")
    })
    @PostMapping("/calender")
    public BaseResponse<?> createIndividualPlan(
            @AuthUser User user,
            @RequestBody CreateIndividualPlanRequestDTO request
    ) {
        userCommandService.createIndividualPlan(user, request);
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "개인 일정 삭제", description = "개인의 일정을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @DeleteMapping("/calender/{individualPlanId}")
    public BaseResponse<?> deleteIndividualPlan(
            @IndividualPlanValidation @PathVariable Long individualPlanId
    ) {
        userCommandService.deleteIndividualPlan(individualPlanId);
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "개인 일정 수정", description = "개인의 일정을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @PutMapping("/calender/{individualPlanId}")
    public BaseResponse<?> updateIndividualPlan(
            @IndividualPlanValidation @PathVariable Long individualPlanId,
            @RequestBody CreateIndividualPlanRequestDTO request
    ) {
        userCommandService.updateIndividualPlan(individualPlanId, request);
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "특정 날짜 (유저가 참여 신청한 모임 일정) 조회", description = "특정 날짜에 예정된 (유저가 참여 신청한 모임 일정)을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @GetMapping("/user-moim-plan")
    public BaseResponse<UserDailyPlanPageDTO> getUserDailyMoimPlan(
            @AuthUser User user,
            @Parameter(description = "연도") @RequestParam int year,
            @Parameter(description = "월") @RequestParam int month,
            @Parameter(description = "일") @RequestParam int day,
            @CheckPageValidation @RequestParam(name = "page") int page,
            @CheckSizeValidation @RequestParam(name = "size") int size
    ) {
        return BaseResponse.onSuccess(userQueryService.getUserDailyMoimPlan(user, year, month, day, page, size));
    }

    @Operation(summary = "특정 날짜 (유저의 개인 일정) 조회", description = "특정 날짜에 예정된 (유저의 개인 일정)을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @GetMapping("/user-individual-plan")
    public BaseResponse<UserDailyPlanPageDTO> getUserDailyIndividualPlan(
            @AuthUser User user,
            @Parameter(description = "연도") @RequestParam int year,
            @Parameter(description = "월") @RequestParam int month,
            @Parameter(description = "일") @RequestParam int day,
            @CheckPageValidation @RequestParam(name = "page") int page,
            @CheckSizeValidation @RequestParam(name = "size") int size
    ) {
        return BaseResponse.onSuccess(userQueryService.getUserDailyIndividualPlan(user, year, month, day, page, size));
    }
  
    @Operation(summary = "이벤트 알림 발송", description = "모든 유저에게 이벤트 알림 발송을 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @GetMapping("/alarms/event")
    public BaseResponse<String> sendEventAlarm(@AuthUser User user, @RequestBody EventDTO eventDTO) {
        userCommandService.sendEventAlarm(eventDTO);
        return BaseResponse.onSuccess("이벤트 알림 보내기에 성공하였습니다.");
    }
}
