package com.dev.moim.domain.moim.controller;

import com.dev.moim.domain.moim.dto.*;
import com.dev.moim.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/calenders")
@Tag(name = "모임 캘린더 관련 컨트롤러")
public class MoimCalendarController {

    @PostMapping("/post")
    @Operation(summary = "모임 새로운 일정 추가", description = "모임 관리자 회원만 일정을 추가할 수 있습니다. 상세 스케줄, 알림 설정은 필수 입력 정보는 아닙니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "CREATED", description = "모임 새로운 일정 추가 성공"),
            @ApiResponse(responseCode = "NOT_FOUND", description = "모임 정보를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "BAD_REQUEST", description = "해당 회원은 일정을 추가할 권한이 없습니다.")
    })
    public BaseResponse<CalenderCreateResponse> createCalender(
            @RequestBody CalenderCreateRequest request
    ) {
        return BaseResponse.onSuccess(null);
    }

    @PostMapping("/update")
    @Operation(summary = "모임 일정 수정", description = "모임 일정을 수정 합니다. 모임 관리자 회원만 일정을 수정할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "OK", description = "모임 일정 변경 성공"),
            @ApiResponse(responseCode = "NOT_FOUND", description = "모임 정보를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "BAD_REQUEST", description = "해당 회원은 일정을 수정할 권한이 없습니다.")
    })
    public CalenderUpdateResponse updateCalender(
            @RequestBody CalenderCreateRequest request
    ) {
        return null;
    }

    @GetMapping("")
    @Operation(summary = "모임 일정 조회", description = "모임 일정을 조회 합니다. 모임에 참여하는 멤버만 조회 가능 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "OK", description = "모임 일정 조회 성공"),
            @ApiResponse(responseCode = "NOT_FOUND", description = "모임 정보를 찾을 수 없습니다.")
    })
    public CalenderGetResponse getCalender(
    ) {
        return null;
    }

    @GetMapping("/{moimId}")
    @Operation(summary = "모임 일정 상세 조회", description = "상세 스케줄 정보 : 시간, 스케줄 내용")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "OK", description = "모임 일정 상세 조회 성공"),
            @ApiResponse(responseCode = "NOT_FOUND", description = "모임 정보를 찾을 수 없습니다.")
    })
    public CalenderDetailGetResponse getDetailCalender(@PathVariable Long moimId) {
        return null;
    }
}