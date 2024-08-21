package com.dev.moim.domain.moim.controller;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.controller.enums.MoimRequestType;
import com.dev.moim.domain.moim.dto.*;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.enums.MoimCategory;
import com.dev.moim.domain.moim.service.MoimCommandService;
import com.dev.moim.domain.moim.service.MoimQueryService;
import com.dev.moim.domain.user.dto.UserPreviewListDTO;
import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.security.annotation.AuthUser;
import com.dev.moim.global.validation.annotation.CheckAdminValidation;
import com.dev.moim.global.validation.annotation.CheckCursorValidation;
import com.dev.moim.global.validation.annotation.CheckTakeValidation;
import com.dev.moim.global.validation.annotation.UserMoimValidaton;
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
        MoimPreviewListDTO moimPreviewListDTO = moimQueryService.getPopularMoim();
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "신규 모임 조회 API", description = "신규 모임을 조회 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/moims/new")
    public BaseResponse<MoimPreviewListDTO> getNewMoim(@RequestParam(name = "cursor") Long cursor, @RequestParam(name = "take") Integer take) {
        MoimPreviewListDTO moimPreviewListDTO = moimQueryService.getNewMoim(cursor, take);
        return BaseResponse.onSuccess(moimPreviewListDTO);
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
            @RequestParam(name = "moimRequestType", required = false) MoimRequestType moimRequestType,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "cursor") @CheckCursorValidation Long cursor,
            @RequestParam(name = "take") @CheckTakeValidation Integer take
    ) {
        MoimPreviewListDTO moimPreviewListDTO = moimQueryService.findMoims(moimRequestType, name, cursor, take);
        return BaseResponse.onSuccess(moimPreviewListDTO);
    }

    @Operation(summary = "모임 가입 신청 확인하기 (신청자 기준) API", description = "모임 가입 신청 확인하기 (신청자 기준) _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/moims/my-requests")
    public BaseResponse<MoimJoinRequestListDTO> findMyRequestMoims(
            @AuthUser User user,
            @RequestParam(name = "cursor") @CheckCursorValidation Long cursor,
            @RequestParam(name = "take") @CheckTakeValidation Integer take
    ) {
        MoimJoinRequestListDTO moimJoinRequestListDTO = moimQueryService.findMyRequestMoims(user, cursor, take);
        return BaseResponse.onSuccess(moimJoinRequestListDTO);
    }


    // 모임 스페 이스 api 나누기
    @Operation(summary = "모임 스페이스 정보 API", description = "모임 카테고리, 인원수, 성별, 설명 등을 리턴합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/moims/{moimId}")
    public BaseResponse<MoimDetailDTO> getMoimDetail(@AuthUser User user, @PathVariable Long moimId) {
        MoimDetailDTO moimDetailDTO = moimQueryService.getMoimDetail(user, moimId);
        return BaseResponse.onSuccess(moimDetailDTO);
    }

    @Operation(summary = "모임 소개 영상 불러오기 API", description = "모임 소개 영상 보기. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/moims/{moimId}/introduce-videos")
    public BaseResponse<MoimIntroduceDTO> getIntroduce(@PathVariable Long moimId) {
        MoimIntroduceDTO moimIntroduceDTO = moimQueryService.getIntroduce(moimId);
        return BaseResponse.onSuccess(moimIntroduceDTO);
    }

    @Operation(summary = "모임 멤버 API", description = "모임에 참여한 멤버들을 조회합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/moims/{moimId}/members")
    public BaseResponse<UserPreviewListDTO> getMoimMembers(@PathVariable @UserMoimValidaton Long moimId, @RequestParam(name = "cursor") Long cursor, @RequestParam(name = "take") Integer take) {
        UserPreviewListDTO userPreviewListDTO = moimQueryService.getMoimMembers(moimId, cursor, take);
        return BaseResponse.onSuccess(userPreviewListDTO);
    }

    @Operation(summary = "모임 탈퇴 하기 API", description = "모임을 탈퇴 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping ("/moims/withdraw")
    public BaseResponse<String> withDrawMoim(@AuthUser User user, @RequestBody @Valid WithMoimDTO withMoimDTO) {
        moimCommandService.withDrawMoim(user, withMoimDTO);
        return BaseResponse.onSuccess("탈퇴 신청하였습니다.");
    }

    @Operation(summary = "모임 가입 신청 상태 확인하기 (초기값 0으로 해주세요) API", description = "모임 가입 신청한 멤버들을 확인합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping ("/moims/{moimId}/requests/users")
    public BaseResponse<UserPreviewListDTO> findRequestMember(@AuthUser User user, @PathVariable @CheckAdminValidation Long moimId, @RequestParam(name = "cursor") Long cursor, @RequestParam(name = "take") Integer take) {
        UserPreviewListDTO userPreviewListDTO = moimQueryService.findRequestMember(user, moimId, cursor, take);
        return BaseResponse.onSuccess(userPreviewListDTO);
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

    @Operation(summary = "모임 신청 API", description = "모임을 신청 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims/{moimId}/requests")
    public BaseResponse<String> joinMoim(@AuthUser User user, @PathVariable Long moimId) {
        moimCommandService.joinMoim(user, moimId);
        return BaseResponse.onSuccess("모임 가입에 신청에 성공하였습니다.");
    }

    @Operation(summary = "모임 가입 신청 받아주기 API", description = "모임 가입 신청 받아줍니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims/{moimId}/accept")
    public BaseResponse<String> acceptMoim(@RequestBody @Valid MoimJoinConfirmRequestDTO moimJoinConfirmRequestDTO) {
        moimCommandService.acceptMoim(moimJoinConfirmRequestDTO);
        return BaseResponse.onSuccess("모임 가입에 받아주기에 성공하였습니다.");
    }

    @Operation(summary = "멤버 권한 수정하기 API", description = "멤버 권한을 수정합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PatchMapping("/moims/{moimId}/authorities")
    public BaseResponse<ChangeAuthorityResponseDTO> changeMemberAuthorities(@AuthUser User user, @RequestBody @Valid ChangeAuthorityRequestDTO changeAuthorityRequestDTO) {
        ChangeAuthorityResponseDTO changeAuthorityResponseDTO = moimCommandService.changeMemberAuthorities(user, changeAuthorityRequestDTO);
        return BaseResponse.onSuccess(changeAuthorityResponseDTO);
    }

    @Operation(summary = "가입 거절하기 API", description = "가입을 거절합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims/users/reject")
    public BaseResponse<String> rejectMoims(@RequestBody @Valid MoimJoinConfirmRequestDTO moimJoinConfirmRequestDTO) {
        moimCommandService.rejectMoims(moimJoinConfirmRequestDTO);
        return BaseResponse.onSuccess("거절에 성공하였습니다.");
    }

    @Operation(summary = "모임장 위임하기 API", description = "모임장을 다른 사람에게 위임합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims/users/leader-change")
    public BaseResponse<String> changeMoimLeader(@AuthUser User user, @RequestBody @Valid ChangeMoimLeaderRequestDTO changeMoimLeaderRequestDTO) {
        moimCommandService.changeMoimLeader(user, changeMoimLeaderRequestDTO);
        return BaseResponse.onSuccess("모임장 위임에 성공하였습니다.");
    }

    @Operation(summary = "모임 신청자 확인 API", description = "모임 신청 확인하는 것을 없앱니다.. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims/{moimId}/my-requests/confirm")
    public BaseResponse<String> findMyRequestMoimsConfirm(@AuthUser User user, @PathVariable Long moimId) {
        moimCommandService.findMyRequestMoimsConfirm(user, moimId);
        return BaseResponse.onSuccess("모임 신청 확인하는 것을 없애는 데 성공 하였습니다.");
    }
}
