package com.dev.moim.domain.moim.controller;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.controller.enums.PostRequestType;
import com.dev.moim.domain.moim.dto.post.CommentResponseDTO;
import com.dev.moim.domain.moim.dto.post.CommentResponseListDTO;
import com.dev.moim.domain.moim.dto.post.CreateCommentDTO;
import com.dev.moim.domain.moim.dto.post.CreateCommentResultDTO;
import com.dev.moim.domain.moim.dto.post.CreateMoimPostDTO;
import com.dev.moim.domain.moim.dto.post.CreateMoimPostResultDTO;
import com.dev.moim.domain.moim.dto.post.MoimPostDetailDTO;
import com.dev.moim.domain.moim.dto.post.MoimPostPreviewListDTO;
import com.dev.moim.domain.moim.entity.Comment;
import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.service.PostCommandService;
import com.dev.moim.domain.moim.service.PostQueryService;
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
import org.springframework.data.domain.Slice;
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

    private final PostQueryService postQueryService;
    private final PostCommandService postCommandService;

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
        MoimPostPreviewListDTO moimPostPreviewListDTO = postQueryService.getMoimPostList(user, moimId, postRequestType, cursor, take);
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
        Post post = postQueryService.getMoimPost(user, moimId, postId);
        Boolean postLike = postQueryService.isPostLike(user.getId(), postId);
        return BaseResponse.onSuccess(MoimPostDetailDTO.toMoimPostDetailDTO(post, postLike));
    }

    @Operation(summary = "모임 게시글 작성 API", description = "모임 게시글을 작성 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims/posts")
    public BaseResponse<CreateMoimPostResultDTO> createMoimPost(@AuthUser User user, @RequestBody CreateMoimPostDTO createMoimPostDTO) {
        Post post = postCommandService.createMoimPost(user, createMoimPostDTO);
        return BaseResponse.onSuccess(CreateMoimPostResultDTO.toCreateMoimPostDTO(post));
    }

    @Operation(summary = "댓글 무한 스크롤 API", description = "댓글을 무한 스크롤로 조회합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/moims/{moimId}/posts/{postId}/comments")
    public BaseResponse<CommentResponseListDTO> getcomments(
            @AuthUser User user,
            @PathVariable Long moimId,
            @PathVariable Long postId,
            @Parameter(description = "처음 값은 1로 해주 세요.") @RequestParam(name = "cursor") @CheckCursorValidation Long cursor,
            @RequestParam(name = "take") @CheckTakeValidation Integer take
    ) {
        CommentResponseListDTO commentResponseListDTO = postQueryService.getcomments(user, moimId, postId, cursor, take);
        return BaseResponse.onSuccess(commentResponseListDTO);
    }

    @Operation(summary = "모임 댓글 작성 API", description = "모임 댓글 작성 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims/posts/comments")
    public BaseResponse<CreateCommentResultDTO> createComment(@AuthUser User user, @RequestBody CreateCommentDTO createCommentDTO) {
        Comment comment = postCommandService.createComment(user, createCommentDTO);
        return BaseResponse.onSuccess(CreateCommentResultDTO.toCreateCommentResultDTO(comment));
    }

}
