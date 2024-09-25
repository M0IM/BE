package com.dev.moim.domain.user.controller;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.user.dto.AlarmCountResponseDTO;
import com.dev.moim.domain.user.dto.EventDTO;
import com.dev.moim.domain.user.service.UserQueryService;
import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.firebase.service.FcmService;
import com.dev.moim.global.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarms")
@Tag(name = "알림 관련 컨트롤러")
public class AlarmController {

    private final FcmService fcmService;
    private final UserQueryService userQueryService;

    @Operation(summary = "이벤트 알림 발송", description = "모든 유저에게 이벤트 알림 발송을 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @PostMapping("/event")
    public BaseResponse<String> sendEventAlarm(@AuthUser User user, @RequestBody EventDTO eventDTO) {
        fcmService.sendEventAlarm(user, eventDTO);
        return BaseResponse.onSuccess("이벤트 알림 보내기에 성공하였습니다.");
    }

    @Operation(summary = "알림 남은 숫자 조회", description = "알림 남은 숫자 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @GetMapping("/alarms/count")
    public BaseResponse<AlarmCountResponseDTO> sendEventAlarm(@AuthUser User user) {
        Integer count = userQueryService.countAlarm(user);
        return BaseResponse.onSuccess(AlarmCountResponseDTO.toAlarmCountResponseDTO(count));
    }
}
