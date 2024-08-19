package com.dev.moim.domain.chatting.controller;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.chatting.converter.ChatRoomConverter;
import com.dev.moim.domain.chatting.dto.ChatRoomDTO.*;
import com.dev.moim.domain.chatting.entity.ChatRoom;
import com.dev.moim.domain.chatting.service.ChatRoomCommandService;
import com.dev.moim.domain.chatting.service.ChatRoomQueryService;
import com.dev.moim.domain.user.dto.ChatRoomUserListResponse;
import com.dev.moim.domain.user.service.UserQueryService;
import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.security.annotation.AuthUser;
import com.dev.moim.global.validation.annotation.CheckCursorValidation;
import com.dev.moim.global.validation.annotation.CheckTakeValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat-rooms")
@RequiredArgsConstructor
@Validated
@Tag(name = "채팅방 관련 컨트 롤러")
public class ChatRoomController {

    private final ChatRoomCommandService chatRoomCommandService;
    private final ChatRoomQueryService chatRoomQueryService;

    @Operation(summary = "채팅방 불러 오기 API", description = "채팅방을 무한 스크롤로 불러옵니다. hasNext가 false고 nextCursor가 null이라면 마지막 페이지입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "FAMILY_SPACE4002", description = "존재하지 않는 가족 공간 입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SIZE4001", description = "올바르지 않은 사이즈입니다.")
    })
    @GetMapping("")
    public BaseResponse<ChatRoomResponseList> getChatRooms(
            @Parameter(hidden = true) @AuthUser User user,
            @Parameter(description = "처음 요청은 1로 해주세요.") @CheckCursorValidation @RequestParam(name = "cursor") Long cursor,
            @RequestParam(name = "take") @CheckTakeValidation Integer take
    ) {
       ChatRoomResponseList chatRoomResponseList = chatRoomQueryService.getChatRoomsByUserId(user.getId(), cursor, take);
        return BaseResponse.onSuccess(chatRoomResponseList);
    }

    @Operation(summary = "채팅방 생성하기 API", description = "채팅방을 생성합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "사용자를 찾을 수 없습니다."),
    })
    @PostMapping("")
    public BaseResponse<CreateChatRoomResponse> createChatRoom(
            @Parameter(hidden = true) @AuthUser User user, @RequestBody @Valid CreateChatRoomRequest createChatRoomRequest
    ) {
        ChatRoom chatRoom = chatRoomCommandService.createChatRoom(createChatRoomRequest, user);
        return BaseResponse.onSuccess(ChatRoomConverter.toCreateChatRoomResponse(chatRoom));
    }

    @Operation(summary = "채팅방 수정 하기 API", description = "채팅방 제목, 사진을 수정 합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHATROOM4002", description = "해당 채팅방이 존재하지 않습니다."),
    })
    @PatchMapping("")
    public BaseResponse<UpdateChatRoomResponse> updateChatRoom(
            @RequestBody @Valid UpdateChatRoomRequest updateChatRoomRequest
    ) {
        ChatRoom chatRoom = chatRoomCommandService.updateChatRoom(updateChatRoomRequest);
        return BaseResponse.onSuccess(ChatRoomConverter.toUpdateChatRoomResponse(chatRoom));
    }

    @Operation(summary = "채팅방 나가기 API", description = "채팅방을 나갑니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHATROOM4002", description = "해당 채팅방이 존재하지 않습니다."),
    })
    @PostMapping("/exit")
    public BaseResponse<?> exitChatRoom(
            @Parameter(hidden = true) @AuthUser User user, @RequestBody ExitChatRoomRequest exitChatRoomRequest
    ) {
        chatRoomCommandService.exitChatRoom(exitChatRoomRequest, user);
        return BaseResponse.onSuccess("해당 chatroom을 나갔습니다.");
    }

//    @GetMapping("/{chatRoomId}/users")
//    @Operation(summary = "채팅 방에 속해 있는 유저 불러 오기 API", description = "채팅 방에 속해 있는 유저 불러 올 수 있습 니다.")
//    @ApiResponses({
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHATROOM4002", description = "해당 채팅방이 존재하지 않습니다.")
//    })
//    public BaseResponse<ChatRoomUserListResponse> getUserByChatRoom(@AuthUser User user, @PathVariable Long chatRoomId) {
//        ChatRoomUserListResponse chatRoomUserListResponse = userQueryService.getUserByChatRoom(user, chatRoomId);
//        return BaseResponse.onSuccess(chatRoomUserListResponse);
//    }
}
