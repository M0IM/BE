package com.dev.moim.domain.user.controller;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.user.dto.EventDTO;
import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.firebase.service.FcmService;
import com.dev.moim.global.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarms")
@Tag(name = "알림 관련 컨트롤러")
public class AlarmController {

    private final FcmService fcmService;

    @Operation(summary = "이벤트 알림 발송", description = "모든 유저에게 이벤트 알림 발송을 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @GetMapping("/event")
    public BaseResponse<String> sendEventAlarm(@AuthUser User user, @RequestBody EventDTO eventDTO) {
        fcmService.sendEventAlarm(eventDTO);
        return BaseResponse.onSuccess("이벤트 알림 보내기에 성공하였습니다.");
    }
}
