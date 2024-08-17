package com.dev.moim.domain.moim.controller;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.controller.enums.PostRequestType;
import com.dev.moim.domain.moim.dto.post.*;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "모임 게시글 신고 API", description = "모임 게시글을 신고 / 신고 취소 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims/posts/reports")
    public BaseResponse<String> reportMoimPost(@AuthUser User user, @RequestBody PostReportDTO postReportDTO) {
        postCommandService.reportMoimPost(user, postReportDTO);
        return BaseResponse.onSuccess("게시물 신고가 완료되었습니다.");
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

    @Operation(summary = "모임 대댓글 작성 API", description = "모임 대댓글 작성 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims/posts/comments/comments")
    public BaseResponse<CreateCommentResultDTO> createCommentComment(@AuthUser User user, @RequestBody CreateCommentCommentDTO createCommentCommentDTO) {
        Comment comment = postCommandService.createCommentComment(user, createCommentCommentDTO);
        return BaseResponse.onSuccess(CreateCommentResultDTO.toCreateCommentResultDTO(comment));
    }

    @Operation(summary = "모임 게시글 좋아요/좋아요 취소 API", description = "모임 게시글 좋아요가 되어있으면 취소를 아니면 좋아요  합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims/posts/like")
    public BaseResponse<LikeResultDTO> postLike(@AuthUser User user, @RequestBody PostLikeDTO postLikeDTO) {
        postCommandService.postLike(user, postLikeDTO);
        return BaseResponse.onSuccess(LikeResultDTO.toLikeResultDTO(postQueryService.isPostLike(user.getId(), postLikeDTO.postId())));
    }

    @Operation(summary = "모임 댓글 좋아요/좋아요 취소 API", description = "모임 댓글 좋아요가 되어있으면 취소를 아니면 좋아요  합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims/comments/Like")
    public BaseResponse<LikeResultDTO> commentLike(@AuthUser User user, @RequestBody CommentLikeDTO commentLikeDTO) {
        postCommandService.commentLike(user, commentLikeDTO);
        return BaseResponse.onSuccess(LikeResultDTO.toLikeResultDTO(postQueryService.isCommentLike(user.getId(), commentLikeDTO.commentId())));
    }

    @Operation(summary = "모임 게시글 삭제 API", description = "모임 게시글을 삭제 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @DeleteMapping("/moims/posts/{postId}}")
    public BaseResponse<String> deletePost(@AuthUser User user, @PathVariable Long postId) {
        postCommandService.deletePost(user, postId);
        return BaseResponse.onSuccess("게시글이 삭제 되었습니다.");
    }

}
