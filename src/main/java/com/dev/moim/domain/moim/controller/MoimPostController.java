package com.dev.moim.domain.moim.controller;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.controller.enums.PostRequestType;
import com.dev.moim.domain.moim.dto.post.*;
import com.dev.moim.domain.moim.entity.Comment;
import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.entity.PostBlock;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.repository.UserMoimRepository;
import com.dev.moim.domain.moim.service.PostCommandService;
import com.dev.moim.domain.moim.service.PostQueryService;
import com.dev.moim.domain.user.dto.UserPreviewDTO;
import com.dev.moim.domain.user.service.UserQueryService;
import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.security.annotation.AuthUser;
import com.dev.moim.global.validation.annotation.CheckCursorValidation;
import com.dev.moim.global.validation.annotation.CheckTakeValidation;
import com.dev.moim.global.validation.annotation.UserMoimValidaton;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "모임 게시글 관련 컨트롤러")
@RequiredArgsConstructor
@Validated
public class MoimPostController {

    private final PostQueryService postQueryService;
    private final PostCommandService postCommandService;
    private final UserQueryService userQueryService;
    private final UserMoimRepository userMoimRepository;

    @Operation(summary = "모임 게시판 목록 API", description = "모임 게시판 목록을 조회 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/moims/{moimId}/posts")
    public BaseResponse<MoimPostPreviewListDTO> getMoimPostList(
            @AuthUser User user,
            @PathVariable @UserMoimValidaton Long moimId,
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
        MoimPostDetailDTO postDetailDTO = postQueryService.getMoimPost(user, moimId, postId);
        return BaseResponse.onSuccess(postDetailDTO);
    }

    @Operation(summary = "모임 게시글 작성 API", description = "모임 게시글을 작성 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims/posts")
    public BaseResponse<CreateMoimPostResultDTO> createMoimPost(@AuthUser User user, @RequestBody @Valid CreateMoimPostDTO createMoimPostDTO) {
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
            @Parameter(description = "처음 값은 0로 해주 세요.") @RequestParam(name = "cursor") Long cursor,
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
    public BaseResponse<CreateCommentResultDTO> createComment(@AuthUser User user, @RequestBody @Valid CreateCommentDTO createCommentDTO) {
        Comment comment = postCommandService.createComment(user, createCommentDTO);
        return BaseResponse.onSuccess(CreateCommentResultDTO.toCreateCommentResultDTO(comment));
    }

    @Operation(summary = "모임 대댓글 작성 API", description = "모임 대댓글 작성 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims/posts/comments/comments")
    public BaseResponse<CreateCommentResultDTO> createCommentComment(@AuthUser User user, @RequestBody @Valid CreateCommentCommentDTO createCommentCommentDTO) {
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
    @DeleteMapping("/moims/posts/{postId}")
    public BaseResponse<String> deletePost(@AuthUser User user, @PathVariable Long postId) {
        postCommandService.deletePost(user, postId);
        return BaseResponse.onSuccess("게시글이 삭제 되었습니다.");
    }

    @Operation(summary = "모임 게시글 수정 API", description = "모임 게시글을 수정 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PutMapping("/moims/posts")
    public BaseResponse<UpdatePostResponseDTO> updatePost(@AuthUser User user, @RequestBody @Valid UpdateMoimPostDTO updateMoimPostDTO) {
        Post post = postCommandService.updatePost(user, updateMoimPostDTO);
        return BaseResponse.onSuccess(UpdatePostResponseDTO.toUpdatePostResponseDTO(post));
    }

    @Operation(summary = "모임 게시글 차단 API", description = "모임 게시글을 차단/차단해제 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims/posts/block")
    public BaseResponse<String> blockPost(@AuthUser User user, @RequestBody @Valid PostBlockDTO postBlockDTO) {
        postCommandService.blockPost(user, postBlockDTO);
        return BaseResponse.onSuccess("게시글이 차단 되었습니다.");
    }

    @Operation(summary = "모임 댓글 삭제 API", description = "모임 댓글을 삭제 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims/comments/{commentId}/delete")
    public BaseResponse<String> deleteComment(@AuthUser User user, @PathVariable Long commentId) {
        postCommandService.deleteComment(user, commentId);
        return BaseResponse.onSuccess("댓글이 삭제 되었습니다.");
    }

    @Operation(summary = "모임 댓글 삭제 API", description = "모임 댓글을 삭제 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PutMapping("/moims/comments")
    public BaseResponse<String> updateComment(@AuthUser User user, @RequestBody @Valid CommentUpdateRequestDTO commentUpdateRequestDTO) {
        postCommandService.updateComment(user, commentUpdateRequestDTO);
        return BaseResponse.onSuccess("댓글이 수정 되었습니다.");
    }

    @Operation(summary = "모임 댓글 신고 API", description = "모임 댓글을 신고 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims/comments/reports")
    public BaseResponse<String> reportComment(@AuthUser User user, @RequestBody CommentReportDTO commentReportDTO) {
        postCommandService.reportComment(user, commentReportDTO);
        return BaseResponse.onSuccess("댓글이 신고 되었습니다.");
    }

    @Operation(summary = "모임 댓글 차단 API", description = "모임 댓글을 차단 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims/comments/block")
    public BaseResponse<String> blockComment(@AuthUser User user, @RequestBody CommentBlockDTO commentBlockDTO) {
        postCommandService.blockComment(user, commentBlockDTO);
        return BaseResponse.onSuccess("댓글이 차단 되었습니다.");
    }

    @Operation(summary = "모임 소개 게시물 조회 API", description = "모임 소개 게시물 조회. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/global/posts")
    public BaseResponse<MoimPostPreviewListDTO> getIntroductionPosts(
                                                     @Parameter(description = "처음 값은 1로 해주 세요.") @RequestParam(name = "cursor") @CheckCursorValidation Long cursor,
                                                     @RequestParam(name = "take") @CheckTakeValidation Integer take) {
        MoimPostPreviewListDTO moimPostPreviewListDTO = postQueryService.getIntroductionPosts(cursor, take);
        return BaseResponse.onSuccess(moimPostPreviewListDTO);
    }

    @Operation(summary = "모임 소개 게시물 상세 조회 API", description = "모임 소개 게시물 상세 조회. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/global/posts/{postId}")
    public BaseResponse<MoimPostDetailDTO> getIntroductionPost(@AuthUser User user, @PathVariable Long postId) {
        Post post = postQueryService.getIntroductionPost( postId);
        Boolean postLike = postQueryService.isPostLike(user.getId(), postId);
        Optional<UserMoim> userMoim =  userMoimRepository.findByPost(post);
        return BaseResponse.onSuccess(MoimPostDetailDTO.toMoimPostDetailDTO(post, postLike, userMoim));
    }

    @Operation(summary = "게시물 읽을 사람 (아직 안읽은사람) API", description = "아직 해당 공지사항을 안 읽은 사람을 리턴합니다.. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/moims/{moimId}/posts/{postId}/users/un-read")
    public BaseResponse<List<UserPreviewDTO>> getUnReadUsers(@AuthUser User user, @PathVariable @UserMoimValidaton Long moimId, @PathVariable Long postId) {
        List<UserPreviewDTO> unReadUserListByPost = userQueryService.findUnReadUserListByPost(user, moimId, postId);
        return BaseResponse.onSuccess(unReadUserListByPost);
    }

    @Operation(summary = "가입된 모입 정보 최신순  API", description = "모임에는 무슨 일이 일어날까요?의 정보를 리턴합니다.. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/posts/what")
    public BaseResponse<List<JoinMoimPostsResponseDTO>> getPostsByJoinMoims(@AuthUser User user) {
        List<JoinMoimPostsResponseDTO> joinMoimPostsResponseDTOList = postQueryService.getPostsByJoinMoims(user);
        return BaseResponse.onSuccess(joinMoimPostsResponseDTOList);
    }

    @Operation(summary = "게시물 공지사항 생성 API", description = "공지 사항을 생성합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims/posts/announcement")
    public BaseResponse<Long> createAnnouncement(@AuthUser User user, @RequestBody @Valid AnnouncementRequestDTO announcementRequestDTO) {
        Long postId = postCommandService.createAnnouncement(user, announcementRequestDTO);
        return BaseResponse.onSuccess(postId);
    }

    @Operation(summary = "게시물 공지사항 읽음 표시하기 API", description = "공지 사항을 읽었음을 표시합니다.. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/moims/posts/announcement/confirm")
    public BaseResponse<Void> announcementConfirm(@AuthUser User user, @RequestBody @Valid AnnouncementConfirmRequestDTO announcementRequestDTO) {
        postCommandService.announcementConfirm(user, announcementRequestDTO);
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "모임 차단한 댓글 API", description = "모임 차단한 댓글을 조회 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/moims/posts/comments/block")
    public BaseResponse<List<BlockCommentResponse>> findBlockComments(@AuthUser User user) {
        List<BlockCommentResponse> blockCommentResponseList = postQueryService.findBlockComments(user);
        return BaseResponse.onSuccess(blockCommentResponseList);
    }

    @Operation(summary = "모임 차단한 게시글 조회 API", description = "모임 차단한 게시글 조회 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/moims/posts/block")
    public BaseResponse<List<MoimPostPreviewDTO>> findBlockPosts(@AuthUser User user) {
        List<MoimPostPreviewDTO> moimPostPreviewDTOList = postQueryService.findBlockPosts(user);
        return BaseResponse.onSuccess(moimPostPreviewDTOList);
    }
}
