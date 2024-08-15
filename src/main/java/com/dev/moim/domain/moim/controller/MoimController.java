package com.dev.moim.domain.moim.controller;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.controller.enums.MoimRequestType;
import com.dev.moim.domain.moim.dto.MoimAnnouncementListDTO;
import com.dev.moim.domain.moim.dto.MoimIntroduceDTO;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.enums.MoimCategory;
import com.dev.moim.domain.moim.service.MoimCommandService;
import com.dev.moim.domain.moim.service.MoimQueryService;
import com.dev.moim.domain.user.dto.UserPreviewListDTO;
import com.dev.moim.domain.moim.dto.CreateMoimDTO;
import com.dev.moim.domain.moim.dto.CreateMoimResultDTO;
import com.dev.moim.domain.moim.dto.MoimDetailDTO;
import com.dev.moim.domain.moim.dto.MoimPreviewListDTO;
import com.dev.moim.domain.moim.dto.UpdateMoimDTO;
import com.dev.moim.domain.moim.dto.WithMoimDTO;
import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.security.annotation.AuthUser;
import com.dev.moim.global.validation.annotation.CheckCursorValidation;
import com.dev.moim.global.validation.annotation.CheckTakeValidation;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "모임 관련 컨트롤러")
@RequiredArgsConstructor
@Validated
public class MoimController {

    private final MoimQueryService moimQueryService;
    private final MoimCommandService moimCommandService;


    // 홈 (모집 중인 모임 + 소개 하는 모임)
    @Operation(summary = "인기 모임 조회 API", description = "인기 있는 모임을 조회 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/moims/popular")
    public BaseResponse<MoimPreviewListDTO> getPopularMoim() {

        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "신규 모임 조회 API", description = "신규 모임을 조회 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/moims/new")
    public BaseResponse<MoimPreviewListDTO> getNewMoim() {

        return BaseResponse.onSuccess(null);
    }

    // 내 모임
    @Operation(summary = "내가 활동 중인 모임 확인 API", description = "내가 활동 중인 모임을 확인 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/moims/me")
    public BaseResponse<MoimPreviewListDTO> getMyMoim(@AuthUser User user, @CheckCursorValidation Long cursor, @CheckTakeValidation Integer take) {
        MoimPreviewListDTO moimPreviewListDTO = moimQueryService.getMyMoim(user, cursor, take);
        return BaseResponse.onSuccess(moimPreviewListDTO);
    }
    
    // 모임 생성
    @Operation(summary = "모임 생성 API", description = "모임을 생성 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims")
    public BaseResponse<CreateMoimResultDTO> createMoim(@Parameter(hidden = true) @AuthUser User user, @RequestBody CreateMoimDTO createMoimDTO) {
        Moim moim = moimCommandService.createMoim(user, createMoimDTO);
        return BaseResponse.onSuccess(CreateMoimResultDTO.toCreateMoimResultDTO(moim));
    }

    // 모임 찾기
    @Operation(summary = "모임 찾기 API", description = "category와 검색어에 따라 모임을 찾습니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "moimRequestType", description = "선택한 카테 고리", example = "all"),
            @Parameter(name = "name", description = "해당 이름이 포함된 모임을 찾습니다.", example = "고양이 모임")
    })
    @GetMapping("/moims")
    public BaseResponse<MoimPreviewListDTO> findMoims(
            @RequestParam(name = "moimRequestType") MoimRequestType moimRequestType,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "cursor") @CheckCursorValidation Long cursor,
            @RequestParam(name = "take") @CheckTakeValidation Integer take
    ) {
        MoimPreviewListDTO moimPreviewListDTO = moimQueryService.findMoims(moimRequestType, name, cursor, take);
        return BaseResponse.onSuccess(moimPreviewListDTO);
    }


    // 모임 스페 이스 api 나누기
    @Operation(summary = "모임 스페이스 정보 API", description = "모임 카테고리, 인원수, 성별, 설명 등을 리턴합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/moims/{moimId}")
    public BaseResponse<MoimDetailDTO> getMoimDetail(@PathVariable Long moimId) {
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "모임 공지사항 정보 API", description = "공지 사항 리스트. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/moims/{moimId}/announcement")
    public BaseResponse<MoimAnnouncementListDTO> getAnnouncement(@PathVariable Long moimId) {
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "모임 소개 영상 API", description = "모임 소개 영상 보기. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/moims/{moimId}/introduce")
    public BaseResponse<MoimIntroduceDTO> getIntroduce(@PathVariable Long moimId) {
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "모임 멤버 API", description = "모임에 참여한 멤버들을 조회합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/moims/{moimId}/members")
    public BaseResponse<UserPreviewListDTO> getMoimMembers(@PathVariable Long moimId) {
        UserPreviewListDTO userPreviewListDTO = moimQueryService.getMoimMembers(moimId);
        return BaseResponse.onSuccess(userPreviewListDTO);
    }

    @Operation(summary = "모임 탈퇴 신청 하기 API", description = "모임을 탈퇴 신청을 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping ("/moims/withdraw")
    public BaseResponse<String> withDrawMoim(@AuthUser User user, @RequestBody @Valid WithMoimDTO withMoimDTO) {
        moimCommandService.withDrawMoim(user, withMoimDTO);
        return BaseResponse.onSuccess("탈퇴 신청하였습니다.");
    }

    @Operation(summary = "모임 가입 신청 상태 확인하기 API", description = "모임 가입 신청 상태 확인합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping ("/moims/{moimId}/status")
    public BaseResponse<MoimPreviewListDTO> confirmStatusMoim(@PathVariable Long moimId) {
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "모임 수정 API", description = "모임을 수정 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PutMapping("/moims")
    public BaseResponse<String> modifyMoimInfo(@RequestBody @Valid UpdateMoimDTO updateMoimDTO) {
        moimCommandService.modifyMoimInfo(updateMoimDTO);
        return BaseResponse.onSuccess("모임 수정에 성공하였습니다.");
    }
}
