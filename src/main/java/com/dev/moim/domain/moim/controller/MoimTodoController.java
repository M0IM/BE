package com.dev.moim.domain.moim.controller;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.task.CreateTodoDTO;
import com.dev.moim.domain.moim.dto.task.TodoDetailDTO;
import com.dev.moim.domain.moim.dto.task.TodoPageDTO;
import com.dev.moim.domain.moim.service.TodoCommandService;
import com.dev.moim.domain.moim.service.TodoQueryService;
import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.security.annotation.AuthUser;
import com.dev.moim.global.validation.annotation.*;
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
@Tag(name = "모임 todo 관련 컨트롤러")
public class MoimTodoController {

    private final TodoCommandService todoCommandService;
    private final TodoQueryService todoQueryService;

    @Operation(summary = "모임 todo 생성", description = "모임 관리자 회원이 모임의 멤버들에게 todo를 줄 수 있는 기능입니다. 관리자 회원만 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "MOIM_002", description = "모임 관리자 회원이 아닙니다."),
            @ApiResponse(responseCode = "MOIM_003", description = "모임의 멤버가 아닙니다.")
    })
    @PostMapping("/{moimId}/todo")
    public BaseResponse<Long> createTodo(
            @AuthUser User user,
            @CheckAdminValidation @UserMoimValidaton @PathVariable Long moimId,
            @Valid @RequestBody CreateTodoDTO request
    ) {
        return BaseResponse.onSuccess(todoCommandService.createTodo(user, moimId, request));
    }

    @Operation(summary = "todo 상세 조회 (할당된 유저)", description = "유저가 자신에게 할당된 특정 todo의 세부사항을 상세 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "MOIM_003", description = "모임의 멤버가 아닙니다."),
            @ApiResponse(responseCode = "TODO_001", description = "Todo를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "TODO_002", description = "해당 유저에게 부여된 todo가 아닙니다.")
    })
    @GetMapping("/{moimId}/todo/{todoId}/assignee")
    public BaseResponse<TodoDetailDTO> getTodoDetailForAssignee(
            @AuthUser User user,
            @UserMoimValidaton @PathVariable Long moimId,
            @TodoAssigneeValidation @PathVariable Long todoId
    ) {
        return BaseResponse.onSuccess(todoQueryService.getTotalDetailForAssignee(user, todoId));
    }

    @Operation(summary = "todo 상세 조회 (모임 관리자)", description = "관리자 회원이 특정 모임의 특정 todo의 세부사항을 상세 조회합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "MOIM_002", description = "모임 관리자 회원이 아닙니다."),
            @ApiResponse(responseCode = "TODO_001", description = "Todo를 찾을 수 없습니다.")
    })
    @GetMapping("/{moimId}/todo/{todoId}/admin/detail")
    public BaseResponse<TodoDetailDTO> getTodoDetailForAdmin(
            @AuthUser User user,
            @CheckAdminValidation @PathVariable Long moimId,
            @TodoValidation @PathVariable Long todoId
    ) {
        return BaseResponse.onSuccess(todoQueryService.getTodoDetailForAdmin(todoId));
    }

    // TODO: cursor 초기값 확인 (현재 방식 : 초기값 null)
    @Operation(summary = "todo 할당받은 멤버 리스트 조회 (모임 관리자)", description = "관리자 회원이 특정 모임의 특정 todo를 할당받은 멤버 리스트를 조회합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "MOIM_002", description = "모임 관리자 회원이 아닙니다."),
            @ApiResponse(responseCode = "TODO_001", description = "Todo를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "MOIM_012", description = "user moim을 찾을 수 없습니다.")
    })
    @GetMapping("/{moimId}/todo/{todoId}/admin/assignee-list")
    public BaseResponse<TodoPageDTO> getTodoAssigneeListForAdmin(
            @AuthUser User user,
            @CheckAdminValidation @PathVariable Long moimId,
            @TodoValidation @PathVariable Long todoId,
            @RequestParam(name = "cursor", required = false) Long cursor,
            @RequestParam(name = "take") Integer take
    ) {
        return BaseResponse.onSuccess(todoQueryService.getTodoAssigneeListForAdmin(todoId, cursor, take));
    }

    @Operation(summary = "특정 모임의 todo 리스트 조회 (모임 관리자)", description = "관리자 회원이 특정 모임의 todo 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "MOIM_002", description = "모임 관리자 회원이 아닙니다."),
    })
    @GetMapping("/{moimId}/todo/admin")
    public BaseResponse<TodoPageDTO> getMoimTodoListForAdmin(
            @AuthUser User user,
            @CheckAdminValidation @PathVariable Long moimId,
            @RequestParam(name = "cursor", required = false) Long cursor,
            @RequestParam(name = "take") Integer take
    ) {
        return BaseResponse.onSuccess(todoQueryService.getMoimTodoListForAdmin(moimId, cursor, take));
    }

    @Operation(summary = "특정 모임 관리자 회원이 부여한 todo 리스트 조회 (모임 관리자)", description = "특정 관리자 회원이 특정 모임에서 자신이 부여한 todo 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "MOIM_002", description = "모임 관리자 회원이 아닙니다."),
    })
    @GetMapping("/{moimId}/todo/admin/self")
    public BaseResponse<TodoPageDTO> getMoimTodoListBySpecificAdmin(
            @AuthUser User user,
            @CheckAdminValidation @PathVariable Long moimId,
            @RequestParam(name = "cursor", required = false) Long cursor,
            @RequestParam(name = "take") Integer take
    ) {
        return BaseResponse.onSuccess(todoQueryService.getMoimTodoListBySpecificAdmin(user, moimId, cursor, take));
    }
}
