package com.dev.moim.domain.moim.controller;

import com.dev.moim.domain.moim.dto.calender.*;
import com.dev.moim.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/moim")
@Tag(name = "모임 캘린더 관련 컨트롤러")
public class MoimCalendarController {

    @Operation(summary = "모임 일정 조회", description = "달력에서 특정 날짜에 등록되어 있는 일정들을 조회합니다. 모임에 참여하는 멤버만 조회 가능 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "모임 일정 조회 성공"),
            @ApiResponse(responseCode = "MOIM_001", description = "모임을 찾을 수 없습니다.")
    })
    @GetMapping("/{moimId}/calender")
    public BaseResponse<PlanListDTO> getPlans(
            @PathVariable Long moimId,
            @Parameter(description = "날짜", example = "yyyy-MM-dd") @RequestParam LocalDate date
    ) {
        return null;
    }

    @Operation(summary = "모임 새로운 일정 추가", description = "모임 관리자 회원만 일정을 추가할 수 있습니다. 상세 스케줄, 알림 설정은 필수 입력 정보는 아닙니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON201", description = "모임 새로운 일정 추가 성공"),
            @ApiResponse(responseCode = "MOIM_001", description = "모임을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "MOIM_002", description = "모임 관리자 회원이 아닙니다.")
    })
    @PostMapping("/{moimId}/calender")
    public BaseResponse<Long> createPlan(
            @PathVariable Long moimId,
            @RequestBody PlanCreateDTO request
    ) {
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "모임 일정 세부사항 조회", description = "특정 일정의 상세 내용과 일정 스케줄을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "모임 일정 상세 조회 성공"),
            @ApiResponse(responseCode = "MOIM_001", description = "모임 정보를 찾을 수 없습니다.")
    })
    @GetMapping("/{moimId}/plan/{planId}")
    public BaseResponse<PlanDetailDTO> getPlanDetails(
            @PathVariable Long moimId,
            @PathVariable Long planId,
            @Parameter(description = "표시할 일정 스케줄 개수", example = "5") @RequestParam(defaultValue = "5") int scheduleCntLimit
    ) {
        return null;
    }

    @Operation(summary = "모임 일정 스케줄 상세 조회", description = "일정 스케줄들을 조회할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "모임 일정 상세 조회 성공"),
            @ApiResponse(responseCode = "MOIM_001", description = "모임 정보를 찾을 수 없습니다.")
    })
    @GetMapping("/{moimId}/plan/{planId}/schedules")
    public BaseResponse<ScheduleListDTO> getSchedules(
            @PathVariable Long moimId,
            @PathVariable Long planId
    ) {
        return null;
    }

    @Operation(summary = "모임 일정 신청자 조회", description = "일정에 참가 신청한 사용자들을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "모임 일정 상세 조회 성공"),
            @ApiResponse(responseCode = "MOIM_001", description = "모임 정보를 찾을 수 없습니다.")
    })
    @GetMapping("/{moimId}/plan/{planId}/participants")
    public BaseResponse<PlanParticipantListPageDTO> getPlanParticipants(
            @PathVariable Long moimId,
            @PathVariable Long planId,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size
    ) {
        return null;
    }

    @Operation(summary = "모임 일정 수정", description = "모임 관리자 회원만 일정을 수정할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "모임 일정 변경 성공"),
            @ApiResponse(responseCode = "MOIM_001", description = "모임 정보를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "MOIM_002", description = "모임 관리자 회원이 아닙니다.")
    })
    @PutMapping("/{moimId}/plan/{planId}")
    public BaseResponse<?> updatePlan(
            @PathVariable Long moimId,
            @PathVariable Long planId,
            @RequestBody PlanCreateDTO request
    ) {
        return null;
    }
}