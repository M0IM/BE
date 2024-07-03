package com.dev.moim.domain.moim.controller;

import com.dev.moim.domain.moim.dto.MoimRequestDTO;
import com.dev.moim.domain.moim.dto.MoimResponseDTO;
import com.dev.moim.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "모임 게시글 관련 컨트롤러")
public class MoimPostController {

    @Operation(summary = "모임 게시판 목록 API", description = "모임 게시판 목록을 조회 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/moims/{moimId}/posts")
    public BaseResponse<MoimResponseDTO.MoimPostPreviewListDTO> getMoimPostList(@PathVariable Long moimId) {
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "모임 게시글 상세 API", description = "모임 게시글을 상세 보기합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims/{moimId}/posts/{postId}")
    public BaseResponse<MoimResponseDTO.MoimPostDetailDTO> getMoimPost(@PathVariable Long moimId, @PathVariable Long postId) {
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "모임 게시글 작성 API", description = "모임 게시글을 작성 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims/{moimId}/posts")
    public BaseResponse<MoimResponseDTO.CreateMoimPostResultDTO> createMoimPost(@RequestBody MoimRequestDTO.CreateMoimPostDTO createMoimPostDTO) {
        return BaseResponse.onSuccess(null);
    }



}
