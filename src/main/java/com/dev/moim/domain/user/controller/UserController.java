package com.dev.moim.domain.user.controller;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.calender.PlanDetailDTO;
import com.dev.moim.domain.moim.dto.calender.PlanMonthListDTO;
import com.dev.moim.domain.moim.service.CalenderQueryService;
import com.dev.moim.domain.user.dto.UserDailyPlanPageDTO;
import com.dev.moim.domain.user.dto.UserPlanDTO;
import com.dev.moim.domain.user.dto.*;
import com.dev.moim.domain.user.service.ReviewCommandService;
import com.dev.moim.domain.user.service.ReviewQueryService;
import com.dev.moim.domain.user.service.UserCommandService;
import com.dev.moim.domain.user.service.UserQueryService;
import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.security.annotation.AuthUser;
import com.dev.moim.global.validation.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "유저 관련 컨트롤러")
public class UserController {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    private final CalenderQueryService calenderQueryService;
    private final ReviewQueryService reviewQueryService;
    private final ReviewCommandService reviewCommandService;

    @Operation(summary = "(멀티 프로필 도입 ver) 유저 프로필 생성", description = "유저의 프로필을 생성하는 기능입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON201", description = "요청 성공 및 리소스 생성됨"),
            @ApiResponse(responseCode = "MOIM_003", description = "모임의 멤버가 아닙니다.")
    })
    @PostMapping("/profile")
    public BaseResponse<String> createProfile(
            @AuthUser User user,
            @Valid @RequestBody CreateProfileDTO request
    ) {
        userCommandService.createProfile(user, request);
        return BaseResponse.onSuccess("유저 프로필 생성 성공");
    }

    // 유저 대표 프로필 조회
    @Operation(summary = "유저 기본 프로필 조회", description = "유저가 기본으로 설정한 프로필 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "USERPROFILE_001", description = "프로필을 찾을 수 없습니다.")
    })
    @GetMapping("/profile")
    public BaseResponse<ProfileDTO> getProfile(
            @AuthUser User user
    ) {
        return BaseResponse.onSuccess(userQueryService.getProfile(user));
    }

    // 유저 프로필 리스트 조회
    @Operation(summary = "(멀티 프로필 도입 ver) 유저 프로필 리스트 조회", description = "유저 프로필 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @GetMapping("/profile/list")
    public BaseResponse<ProfilePageDTO> getUserProfileList(
            @AuthUser User user,
            @CheckCursorValidation @RequestParam(name = "cursor") Long cursor,
            @CheckTakeValidation @RequestParam(name = "take") Integer take
    ) {
        return BaseResponse.onSuccess(userQueryService.getUserProfileList(user, cursor, take));
    }

    // 유저 멀티 프로필 수정
    // TODO: residence 프로필마다 설정 가능 or default 정보로
    // TODO: 대표 프로필 설정 여부
    @Operation(summary = "(멀티 프로필 도입 ver) 유저 멀티 프로필 수정", description = "유저의 멀티 프로필을 수정하는 기능입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON201", description = "요청 성공 및 리소스 생성됨"),
            @ApiResponse(responseCode = "MOIM_003", description = "모임의 멤버가 아닙니다."),
            @ApiResponse(responseCode = "USERPROFILE_001", description = "프로필을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "USERPROFILE_003", description = "해당 유저의 프로필이 아닙니다."),
    })
    @PutMapping("/profile/{profileId}")
    public BaseResponse<String> updateUserProfile(
            @AuthUser User user,
            @ProfileOwnerValidation @PathVariable Long profileId,
            @Valid @RequestBody UpdateMultiProfileDTO request
    ) {
        userCommandService.updateUserProfile(user, profileId, request);
        return BaseResponse.onSuccess("멀티 프로필 수정 성공했습니다.");
    }

    @Operation(summary = "(멀티 프로필 도입 전 ver) 유저 프로필 수정", description = "(멀티 프로필 도입 전 ver) 유저의 프로필을 수정하는 기능입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON201", description = "요청 성공 및 리소스 생성됨"),
    })
    @PutMapping("/profile")
    public BaseResponse<Void> updateUserInfo(
            @AuthUser User user,
            @Valid @RequestBody UpdateUserInfoDTO request
    ) {
        userCommandService.updateUserInfo(user, request);
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "(멀티 프로필 도입 ver) 유저 기본 정보 수정", description = "유저의 기본 정보를 수정하는 기능입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON201", description = "요청 성공 및 리소스 생성됨"),
    })
    @PutMapping("/profile/default-info")
    public BaseResponse<Void> updateUserDefaultInfo(
            @AuthUser User user,
            @Valid @RequestBody UpdateUserDefaultInfoDTO request
    ) {
        userCommandService.updateUserDefaultInfo(user, request);
        return BaseResponse.onSuccess(null);
    }

    // 특정 프로필 삭제
    // TODO: 해당 프로필이 대표 프로필인 경우 처리 -> 현재 : 에러 처리
    // TODO: 해당 프로필을 사용중인 모임이 있는 경우 처리 -> 현재 : 에러 처리
    @Operation(summary = "(멀티 프로필 도입 ver) 유저 프로필 삭제", description = "유저의 특정 프로필을 삭제하는 기능입니다." +
            " 대표 프로필이거나 해당 프로필을 사용중인 모임이 있는 경우 삭제 불가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "USERPROFILE_001", description = "프로필을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "USERPROFILE_003", description = "해당 유저의 프로필이 아닙니다."),
            @ApiResponse(responseCode = "USERPROFILE_004", description = "해당 프로필을 사용 중인 모임이 있습니다."),
            @ApiResponse(responseCode = "USERPROFILE_005", description = "대표 프로필은 삭제할 수 없습니다. 대표 프로필을 변경해주세요.")
    })
    @DeleteMapping(("/profile/{profileId}"))
    public BaseResponse<String> deleteUserProfile(
            @AuthUser User user,
            @DeletableProfileValidation @PathVariable Long profileId
    ) {
        userCommandService.deleteUserProfile(profileId);
        return BaseResponse.onSuccess("프로필 삭제 성공했습니다.");
    }

    // TODO: 특정 프로필 상세 조회로 변경
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

    // TODO: moimId 받는걸로 수정 (해당 모임에서의 프로필 조회)
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
        return BaseResponse.onSuccess(reviewQueryService.getUserReviews(user.getId(), page, size));
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
        return BaseResponse.onSuccess(reviewQueryService.getUserReviews(userId, page, size));
    }

    @Operation(summary = "멤버 후기 작성", description = "유저 본인에겐 후기를 남길 수 없습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "AUTH_020", description = "존재하지 않는 사용자입니다."),
            @ApiResponse(responseCode = "REVIEW_001", description = "유저 본인에게 리뷰를 남길 수 없습니다.")
    })
    @PostMapping("/reviews")
    public BaseResponse<CreateReviewResultDTO> postMemberReview(
            @AuthUser User user,
            @Valid @RequestBody CreateReviewDTO createReviewDTO) {
        return BaseResponse.onSuccess(reviewCommandService.postMemberReview(user, createReviewDTO));
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
    @GetMapping("/alarms/status")
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

    @Operation(summary = "특정 날짜 (연, 월) : 개인 일정 리스트 조회", description = "유저의 개인 일정들을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "유저 일정 조회 성공"),
    })
    @GetMapping("/monthly/individual-plans")
    public BaseResponse<PlanMonthListDTO<List<UserPlanDTO>>> getIndividualPlans(
            @AuthUser User user,
            @Parameter(description = "연도") @RequestParam int year,
            @Parameter(description = "월") @RequestParam int month
    ) {
        return BaseResponse.onSuccess(userQueryService.getIndividualPlans(user, year, month));
    }

    @Operation(summary = "특정 날짜 (연, 월) : 유저가 참여 신청한 모임 일정 리스트 조회", description = "유저가 참여 신청한 모임 일정들을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @GetMapping("/monthly/moim-plans")
    public BaseResponse<PlanMonthListDTO<List<UserPlanDTO>>> getUserPlans(
            @AuthUser User user,
            @Parameter(description = "연도") @RequestParam int year,
            @Parameter(description = "월") @RequestParam int month
    ) {
        return BaseResponse.onSuccess(userQueryService.getUserPlans(user, year, month));
    }

    @Operation(summary = "특정 날짜 (연, 월) : 모든 타입 일정 리스트 조회", description = "유저의 모든 타입 일정들 (참여 신청한 모임 일정 + 개인 일정) 을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "유저 일정 조회 성공"),
    })
    @GetMapping("/monthly/total-plans")
    public BaseResponse<PlanMonthListDTO<List<UserPlanDTO>>> getUserMonthlyPlans(
            @AuthUser User user,
            @Parameter(description = "연도") @RequestParam int year,
            @Parameter(description = "월") @RequestParam int month
    ) {
        return BaseResponse.onSuccess(userQueryService.getUserMonthlyPlans(user, year, month));
    }

    @Operation(summary = "특정 날짜 (연, 월, 일) : 유저가 참여 신청한 모임 일정 리스트 조회", description = "특정 날짜에 예정된 (유저가 참여 신청한 모임 일정)을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @GetMapping("/daily/moim-plans")
    public BaseResponse<UserDailyPlanPageDTO> getUserDailyMoimPlans(
            @AuthUser User user,
            @Parameter(description = "연도") @RequestParam int year,
            @Parameter(description = "월") @RequestParam int month,
            @Parameter(description = "일") @RequestParam int day,
            @CheckPageValidation @RequestParam(name = "page") int page,
            @CheckSizeValidation @RequestParam(name = "size") int size
    ) {
        return BaseResponse.onSuccess(userQueryService.getUserDailyMoimPlans(user, year, month, day, page, size));
    }

    @Operation(summary = "특정 날짜 (연, 월, 일) : 유저의 개인 일정 리스트 조회", description = "특정 날짜에 예정된 (유저의 개인 일정)을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @GetMapping("/daily/individual-plans")
    public BaseResponse<UserDailyPlanPageDTO> getUserDailyIndividualPlans(
            @AuthUser User user,
            @Parameter(description = "연도") @RequestParam int year,
            @Parameter(description = "월") @RequestParam int month,
            @Parameter(description = "일") @RequestParam int day,
            @CheckPageValidation @RequestParam(name = "page") int page,
            @CheckSizeValidation @RequestParam(name = "size") int size
    ) {
        return BaseResponse.onSuccess(userQueryService.getUserDailyIndividualPlans(user, year, month, day, page, size));
    }

    @Operation(summary = "특정 날짜 (연, 월, 일) : 유저의 (개인 일정 + 모임 신청 일정 + 할당받은 todo) 리스트 조회", description = "특정 날짜에 예정된 (개인 + 모임 신청 일정) 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @GetMapping("/daily/total-plans")
    public BaseResponse<UserDailyPlanPageDTO> getUserDailyPlans(
            @AuthUser User user,
            @Parameter(description = "연도") @RequestParam int year,
            @Parameter(description = "월") @RequestParam int month,
            @Parameter(description = "일") @RequestParam int day,
            @CheckPageValidation @RequestParam(name = "page") int page,
            @CheckSizeValidation @RequestParam(name = "size") int size
    ) {
        return BaseResponse.onSuccess(userQueryService.getUserDailyPlans(user, year, month, day, page, size));
    }

    @Operation(summary = "특정 날짜 (연, 월, 일) : 유저의 총 일정 개수 조회", description = "특정 날짜에 예정된 유저의 총 일정 개수 (개인 일정 + 모임 신청 일정 + 할당받은 todo) 를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @GetMapping("/user-plan-count")
    public BaseResponse<UserDailyPlanCntDTO> getUserDailyPlanCnt (
            @AuthUser User user,
            @Parameter(description = "연도") @RequestParam int year,
            @Parameter(description = "월") @RequestParam int month,
            @Parameter(description = "일") @RequestParam int day
    ) {
        return BaseResponse.onSuccess(userQueryService.getUserDailyPlanCnt(user, year, month, day));
    }

    @Operation(summary = "유저의 개인 일정 상세 조회", description = "유저의 특정 개인 일정을 상세 조회 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "PLAN_001", description = "존재하지 않는 개인 일정 입니다."),
            @ApiResponse(responseCode = "PLAN_002", description = "해당 일정의 작성자가 아닙니다.")
    })
    @GetMapping("/individual-plan/{individualPlanId}")
    public BaseResponse<UserPlanDTO> getIndividualPlanDetail (
            @AuthUser User user,
            @IndividualPlanValidation @PathVariable Long individualPlanId
    ) {
        return BaseResponse.onSuccess(userQueryService.getIndividualPlanDetail(user, individualPlanId));
    }

    @Operation(summary = "유저의 모임 참여 신청 일정 상세 조회", description = "유저의 특정 모임 참여 신청 일정을 상세 조회 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "PLAN_002", description = "해당 일정에 참여 신청하지 않았습니다."),
            @ApiResponse(responseCode = "PLAN_005", description = "존재하지 않는 일정입니다.")
    })
    @GetMapping("/moim-plan/{userMoimPlanId}")
    public BaseResponse<PlanDetailDTO> getUserMoimPlanDetail (
            @AuthUser User user,
            @UserPlanValidation @PathVariable Long userMoimPlanId
    ) {
        return BaseResponse.onSuccess(calenderQueryService.getPlanDetails(user, userMoimPlanId));
    }

    @Operation(summary = "알림 삭제 API", description = "모든 알림을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @DeleteMapping("/alarms")
    public BaseResponse<String> deleteAlarms(@AuthUser User user) {
        userCommandService.deleteAlarms(user);
        return BaseResponse.onSuccess("모든 알림을 삭제하였습니다.");
    }

    @Operation(summary = "알림 목록 API", description = "알림 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @GetMapping("/alarms")
    public BaseResponse<AlarmResponseListDTO> getAlarms(@AuthUser User user, @RequestParam("cursor") Long cursor, @RequestParam("take") Integer take) {
        AlarmResponseListDTO alarms = userQueryService.getAlarms(user, cursor, take);
        return BaseResponse.onSuccess(alarms);
    }
}
