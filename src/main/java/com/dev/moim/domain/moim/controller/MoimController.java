package com.dev.moim.domain.moim.controller;

import com.dev.moim.domain.account.dto.UserResponseDTO;
import com.dev.moim.domain.moim.dto.MoimRequestDTO;
import com.dev.moim.domain.moim.dto.MoimResponseDTO;
import com.dev.moim.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "모임 관련 컨트롤러")
public class MoimController {

    @Operation(summary = "모임 생성 API", description = "모임을 생성 합니다. _by 제이미_")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims")
    public ApiResponse<MoimResponseDTO.CreateMoimResultDTO> createMoim(@RequestBody MoimRequestDTO.CreateMoimDTO createMoimDTO) {

        return ApiResponse.onSuccess(null);
    }

    @Operation(summary = "모임 찾기 API", description = "category와 검색어에 따라 모임을 찾습니다. _by 제이미_")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "type", description = "'category' 인지 'search'  인지", example = "category"),
            @Parameter(name = "keyword", description = "선택한 카테 고리 혹은 검색어", example = "all")
    })
    @GetMapping("/moims")
    public ApiResponse<MoimResponseDTO.MoimPreviewListDTO> findMoim(@RequestParam(name = "type") String type, @RequestParam(name = "keyword") String keyword) {
        return ApiResponse.onSuccess(null);
    }

    @Operation(summary = "내가 활동 중인 모임 확인 API", description = "내가 활동 중인 모임을 확인 합니다. _by 제이미_")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/moims/active")
    public ApiResponse<MoimResponseDTO.MoimPreviewListDTO> activeMoim() {
        return ApiResponse.onSuccess(null);
    }

    @Operation(summary = "모임 스페이스 메인 화면 API", description = "모임 메인 화면 상세 보기를 합니다. _by 제이미_")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/moims/{moimId}")
    public ApiResponse<MoimResponseDTO.MoimDetailDTO> getMoimDetail(@PathVariable Long moimId) {
        return ApiResponse.onSuccess(null);
    }

    @Operation(summary = "모임 멤버 API", description = "모임에 참여한 멤버들을 조회합니다. _by 제이미_")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/moims/{moimId}/members")
    public ApiResponse<UserResponseDTO.UserPreviewListDTO> getMoimMembers(@PathVariable Long moimId) {
        return ApiResponse.onSuccess(null);
    }

    @Operation(summary = "모임 탈퇴 관리 하기 API", description = "모임을 탈퇴합니다. _by 제이미_")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping ("/moims/withdraw")
    public ApiResponse<String> withDrawMoim(@RequestBody MoimRequestDTO.WithMoimDTO withMoimDTO) {
        return ApiResponse.onSuccess(null);
    }

    @Operation(summary = "모임 가입 신청 상태 확인하기 API", description = "모임 가입 신청 상태 확인합니다. _by 제이미_")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping ("/moims/{moimId}/status")
    public ApiResponse<MoimResponseDTO.MoimPreviewListDTO> confirmStatusMoim(@PathVariable Long moimId) {
        return ApiResponse.onSuccess(null);
    }

    @Operation(summary = "모임 생성 API", description = "모임을 생성 합니다. _by 제이미_")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping ("/moims/{moimId}")
    public ApiResponse<String> modifyMoimInfo(@RequestBody MoimRequestDTO.UpdateMoimDTO updateMoimDTO) {
        return ApiResponse.onSuccess(null);
    }
}
