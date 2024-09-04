package com.dev.moim.domain.moim.controller;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.task.*;
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
@RequestMapping("/api/v1")
@Tag(name = "todo 관련 컨트롤러")
public class MoimTodoController {

    private final TodoCommandService todoCommandService;
    private final TodoQueryService todoQueryService;

    @Operation(summary = "모임 todo 생성", description = "모임 관리자 회원이 모임의 멤버들에게 todo를 줄 수 있는 기능입니다. 관리자 회원만 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "MOIM_002", description = "모임 관리자 회원이 아닙니다."),
            @ApiResponse(responseCode = "MOIM_003", description = "모임의 멤버가 아닙니다."),
            @ApiResponse(responseCode = "TODO_004", description = "Todo를 할당받을 유저를 지정하지 않았습니다."),
            @ApiResponse(responseCode = "TODO_005", description = "전체 선택인 경우 특정 assignee를 지정할 수 없습니다.")
    })
    @PostMapping("/moims/{moimId}/todos")
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
    @GetMapping("/moims/{moimId}/todos/{todoId}/for-me")
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
    @GetMapping("/moims/{moimId}/todos/{todoId}/admins/detail")
    public BaseResponse<TodoDetailDTO> getTodoDetailForAdmin(
            @AuthUser User user,
            @CheckAdminValidation @PathVariable Long moimId,
            @TodoValidation @PathVariable Long todoId
    ) {
        return BaseResponse.onSuccess(todoQueryService.getTodoDetailForAdmin(todoId));
    }

    @Operation(summary = "todo 할당받은 멤버 리스트 조회 (모임 관리자)", description = "관리자 회원이 특정 모임의 특정 todo를 할당받은 멤버 리스트를 조회합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "MOIM_002", description = "모임 관리자 회원이 아닙니다."),
            @ApiResponse(responseCode = "TODO_001", description = "Todo를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "MOIM_012", description = "user moim을 찾을 수 없습니다.")
    })
    @GetMapping("/moims/{moimId}/todos/{todoId}/admins/assignee-list")
    public BaseResponse<TodoPageDTO> getTodoAssigneeListForAdmin(
            @AuthUser User user,
            @CheckAdminValidation @PathVariable Long moimId,
            @TodoValidation @PathVariable Long todoId,
            @CheckCursorValidation @RequestParam(name = "cursor") Long cursor,
            @CheckTakeValidation @RequestParam(name = "take") Integer take
    ) {
        return BaseResponse.onSuccess(todoQueryService.getTodoAssigneeListForAdmin(todoId, cursor, take));
    }

    @Operation(summary = "todo 할당받은 멤버 제외한 모임 멤버 조회 (모임 관리자)", description = "관리자 회원이 특정 모임의 특정 todo를 할당받은 멤버를 제외한 모임 멤버들을 조회합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "MOIM_002", description = "모임 관리자 회원이 아닙니다."),
            @ApiResponse(responseCode = "TODO_001", description = "Todo를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "MOIM_012", description = "user moim을 찾을 수 없습니다.")
    })
    @GetMapping("/moims/{moimId}/todos/{todoId}/admins/non-assignee-list")
    public BaseResponse<TodoPageDTO> getTodoNonAssigneeListForAdmin(
            @AuthUser User user,
            @CheckAdminValidation @PathVariable Long moimId,
            @TodoValidation @PathVariable Long todoId,
            @CheckCursorValidation @RequestParam(name = "cursor") Long cursor,
            @CheckTakeValidation @RequestParam(name = "take") Integer take
    ) {
        return BaseResponse.onSuccess(todoQueryService.getTodoNonAssigneeListForAdmin(moimId, todoId, cursor, take));
    }

    @Operation(summary = "특정 모임의 todo 리스트 조회 (모임 관리자)", description = "관리자 회원이 특정 모임의 todo 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "MOIM_002", description = "모임 관리자 회원이 아닙니다."),
    })
    @GetMapping("/moims/{moimId}/todos/admins")
    public BaseResponse<TodoPageDTO> getMoimTodoListForAdmin(
            @AuthUser User user,
            @CheckAdminValidation @PathVariable Long moimId,
            @CheckCursorValidation  @RequestParam(name = "cursor") Long cursor,
            @CheckTakeValidation @RequestParam(name = "take") Integer take
    ) {
        return BaseResponse.onSuccess(todoQueryService.getMoimTodoListForAdmin(moimId, cursor, take));
    }

    @Operation(summary = "특정 모임 관리자 회원이 부여한 todo 리스트 조회 (모임 관리자)", description = "특정 관리자 회원이 특정 모임에서 자신이 부여한 todo 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "MOIM_002", description = "모임 관리자 회원이 아닙니다."),
    })
    @GetMapping("/moims/{moimId}/todos/by-me")
    public BaseResponse<TodoPageDTO> getSpecificMoimTodoListByMe(
            @AuthUser User user,
            @CheckAdminValidation @PathVariable Long moimId,
            @CheckCursorValidation  @RequestParam(name = "cursor") Long cursor,
            @CheckTakeValidation @RequestParam(name = "take") Integer take
    ) {
        return BaseResponse.onSuccess(todoQueryService.getSpecificMoimTodoListByMe(user, moimId, cursor, take));
    }

    @Operation(summary = "자신이 부여한 todo 리스트 조회", description = "회원이 자신이 부여한 todo 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @GetMapping("/todos/by-me")
    public BaseResponse<TodoPageDTO> getTodoListByMe(
            @AuthUser User user,
            @RequestParam(name = "cursor", required = false) Long cursor,
            @RequestParam(name = "take") Integer take
    ) {
        return BaseResponse.onSuccess(todoQueryService.getTodoListByMe(user, cursor, take));
    }

    @Operation(summary = "부여된 todo 상태 업데이트", description = "회원이 자신에게 부여된 todo의 상태를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "MOIM_003", description = "모임의 멤버가 아닙니다."),
            @ApiResponse(responseCode = "TODO_001", description = "Todo를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "TODO_002", description = "해당 유저에게 부여된 todo가 아닙니다."),
            @ApiResponse(responseCode = "TODO_003", description = "업데이트 요청한 todo status가 기존 status와 동일합니다."),
            @ApiResponse(responseCode = "TODO_006", description = "마감 기한이 지난 Todo입니다."),
            @ApiResponse(responseCode = "TODO_007", description = "해당 todo status로 변경할 수 없습니다.")
    })
    @PutMapping("/moims/{moimId}/todos/assignee/{todoId}")
    public BaseResponse<UpdateTodoStatusResponseDTO> updateUserTodoStatus(
            @AuthUser User user,
            @UserMoimValidaton @PathVariable Long moimId,
            @TodoAssigneeValidation @PathVariable Long todoId,
            @Valid @RequestBody UpdateTodoStatusDTO request
    ) {
        return BaseResponse.onSuccess(todoCommandService.updateUserTodoStatus(user, todoId, request));
    }

    @Operation(summary = "todo 수정", description = "모임 관리자 회원이 특정 todo 내용을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "MOIM_002", description = "모임 관리자 회원이 아닙니다."),
            @ApiResponse(responseCode = "MOIM_003", description = "모임의 멤버가 아닙니다."),
            @ApiResponse(responseCode = "TODO_001", description = "Todo를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "TODO_004", description = "Todo를 할당받을 유저를 지정하지 않았습니다."),
            @ApiResponse(responseCode = "TODO_005", description = "전체 선택인 경우 특정 assignee를 지정할 수 없습니다.")
    })
    @PutMapping("/moims/{moimId}/todos/admin/{todoId}")
    public BaseResponse<?> updateTodo(
            @AuthUser User user,
            @CheckAdminValidation @PathVariable Long moimId,
            @TodoValidation @PathVariable Long todoId,
            @RequestBody UpdateTodoDTO request
    ) {
        todoCommandService.updateTodo(user, moimId, todoId, request);
        return BaseResponse.onSuccess("todo 수정 성공했습니다.");
    }

    @Operation(summary = "todo 삭제", description = "모임 관리자 회원이 특정 todo 내용을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "MOIM_002", description = "모임 관리자 회원이 아닙니다."),
            @ApiResponse(responseCode = "MOIM_003", description = "모임의 멤버가 아닙니다."),
            @ApiResponse(responseCode = "TODO_001", description = "Todo를 찾을 수 없습니다.")
    })
    @DeleteMapping("/moims/{moimId}/todos/admin/{todoId}")
    public BaseResponse<?> deleteTodo(
            @AuthUser User user,
            @CheckAdminValidation @PathVariable Long moimId,
            @TodoValidation @PathVariable Long todoId
    ) {
        todoCommandService.deleteTodo(todoId);
        return BaseResponse.onSuccess("todo 삭제 성공했습니다.");
    }

    @Operation(summary = "todo assignee 추가", description = "모임 관리자 회원이 특정 todo를 할당받을 모임 멤버를 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "MOIM_002", description = "모임 관리자 회원이 아닙니다."),
            @ApiResponse(responseCode = "MOIM_003", description = "모임의 멤버가 아닙니다."),
            @ApiResponse(responseCode = "TODO_001", description = "Todo를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "TODO_008", description = "이미 todo를 할당받은 멤버를 지정했습니다.")
    })
    @PutMapping("/moims/todos/admin/assignees/new")
    public BaseResponse<?> addAssignees(
            @AuthUser User user,
            @Valid @RequestBody AddTodoAssigneeDTO request
    ) {
        todoCommandService.addAssignees(user, request);
        return BaseResponse.onSuccess("todo assignee 추가 성공했습니다.");
    }

    @Operation(summary = "todo assignee 삭제", description = "모임 관리자 회원이 특정 todo를 할당받을 모임 멤버를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "MOIM_002", description = "모임 관리자 회원이 아닙니다."),
            @ApiResponse(responseCode = "MOIM_003", description = "모임의 멤버가 아닙니다."),
            @ApiResponse(responseCode = "TODO_001", description = "Todo를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "TODO_002", description = "해당 유저에게 부여된 todo가 아닙니다.")
    })
    @PutMapping("/moims/todos/admin/assignees/current")
    public BaseResponse<?> deleteAssignees(
            @AuthUser User user,
            @Valid @RequestBody DeleteTodoAssigneeDTO request
    ) {
        todoCommandService.deleteAssignees(request);
        return BaseResponse.onSuccess("todo assignee 삭제 성공했습니다.");
    }
}
