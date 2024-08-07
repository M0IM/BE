package com.dev.moim.domain.moim.controller;

import com.dev.moim.domain.moim.dto.MoimAnnouncementListDTO;
import com.dev.moim.domain.moim.dto.MoimIntroduceDTO;
import com.dev.moim.domain.user.dto.UserPreviewListDTO;
import com.dev.moim.domain.moim.dto.CreateMoimDTO;
import com.dev.moim.domain.moim.dto.CreateMoimResultDTO;
import com.dev.moim.domain.moim.dto.MoimDetailDTO;
import com.dev.moim.domain.moim.dto.MoimPreviewListDTO;
import com.dev.moim.domain.moim.dto.UpdateMoimDTO;
import com.dev.moim.domain.moim.dto.WithMoimDTO;
import com.dev.moim.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @GetMapping("/moims/active")
    public BaseResponse<MoimPreviewListDTO> activeMoim() {
        return BaseResponse.onSuccess(null);
    }
    
    // 모임 생성
    @Operation(summary = "모임 생성 API", description = "모임을 생성 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims")
    public BaseResponse<CreateMoimResultDTO> createMoim(@RequestBody CreateMoimDTO createMoimDTO) {

        return BaseResponse.onSuccess(null);
    }

    // 모임 찾기
    @Operation(summary = "모임 찾기 API", description = "category와 검색어에 따라 모임을 찾습니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "type", description = "'category' 인지 'search'  인지", example = "category"),
            @Parameter(name = "keyword", description = "선택한 카테 고리 혹은 검색어", example = "all")
    })
    @GetMapping("/moims")
    public BaseResponse<MoimPreviewListDTO> findMoim(@RequestParam(name = "type") String type, @RequestParam(name = "keyword") String keyword) {
        return BaseResponse.onSuccess(null);
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
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "모임 탈퇴 관리 하기 API", description = "모임을 탈퇴합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping ("/moims/withdraw")
    public BaseResponse<String> withDrawMoim(@RequestBody WithMoimDTO withMoimDTO) {
        return BaseResponse.onSuccess(null);
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
    @PostMapping ("/moims/{moimId}")
    public BaseResponse<String> modifyMoimInfo(@RequestBody UpdateMoimDTO updateMoimDTO) {
        return BaseResponse.onSuccess(null);
    }
}
