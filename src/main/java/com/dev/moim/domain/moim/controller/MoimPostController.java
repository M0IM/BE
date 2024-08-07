package com.dev.moim.domain.moim.controller;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.controller.enums.PostRequestType;
import com.dev.moim.domain.moim.dto.CreateMoimPostDTO;
import com.dev.moim.domain.moim.dto.CreateMoimPostResultDTO;
import com.dev.moim.domain.moim.dto.MoimPostDetailDTO;
import com.dev.moim.domain.moim.dto.MoimPostPreviewListDTO;
import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.service.MoimPostQueryService;
import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.security.annotation.AuthUser;
import com.dev.moim.global.validation.annotation.CheckCursorValidation;
import com.dev.moim.global.validation.annotation.CheckTakeValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "모임 게시글 관련 컨트롤러")
@RequiredArgsConstructor
@Validated
public class MoimPostController {

    private final MoimPostQueryService moimPostQueryService;

    @Operation(summary = "모임 게시판 목록 API", description = "모임 게시판 목록을 조회 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/moims/{moimId}/posts")
    public BaseResponse<MoimPostPreviewListDTO> getMoimPostList(
            @AuthUser User user,
            @PathVariable Long moimId,
            @RequestParam(name = "postType") PostRequestType postRequestType,
            @Parameter(description = "처음 값은 1로 해주 세요.") @RequestParam(name = "cursor") @CheckCursorValidation Long cursor,
            @RequestParam(name = "take") @CheckTakeValidation Integer take
    ) {
        MoimPostPreviewListDTO moimPostPreviewListDTO = moimPostQueryService.getMoimPostList(user, moimId, postRequestType, cursor, take);
        return BaseResponse.onSuccess(moimPostPreviewListDTO);
    }

    @Operation(summary = "모임 게시글 상세 API", description = "모임 게시글을 상세 보기합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/moims/{moimId}/posts/{postId}")
    public BaseResponse<MoimPostDetailDTO> getMoimPost(
            @AuthUser User user,
            @PathVariable Long moimId,
            @PathVariable Long postId
    ) {
        Post post = moimPostQueryService.getMoimPost(user, moimId, postId);
        return BaseResponse.onSuccess(MoimPostDetailDTO.toMoimPostDetailDTO(post));
    }

    @Operation(summary = "모임 게시글 작성 API", description = "모임 게시글을 작성 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims/{moimId}/posts")
    public BaseResponse<CreateMoimPostResultDTO> createMoimPost(@RequestBody CreateMoimPostDTO createMoimPostDTO) {
        return BaseResponse.onSuccess(null);
    }



}
