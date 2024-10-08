package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.post.*;
import com.dev.moim.domain.moim.entity.Comment;
import com.dev.moim.domain.moim.entity.Post;
import jakarta.validation.Valid;

public interface PostCommandService {
    Post createMoimPost(User user, CreateMoimPostDTO createMoimPostDTO);

    Comment createComment(User user, CreateCommentDTO createCommentDTO);

    Comment createCommentComment(User user, CreateCommentCommentDTO createCommentCommentDTO);

    void postLike(User user, PostLikeDTO postLikeDTO);

    void commentLike(User user, CommentLikeDTO commentLikeDTO);

    void reportMoimPost(User user, PostReportDTO postReportDTO);

    void deletePost(User user, Long postId);

    Post updatePost(User user, @Valid UpdateMoimPostDTO updateMoimPostDTO);

    void blockPost(User user, @Valid PostBlockDTO postBlockDTO);

    void deleteComment(User user, Long commentId);

    void updateComment(User user, CommentUpdateRequestDTO commentUpdateRequestDTO);

    void reportComment(User user, CommentReportDTO commentReportDTO);

    void blockComment(User user, CommentBlockDTO commentBlockDTO);

    Long createAnnouncement(User user, @Valid AnnouncementRequestDTO announcementRequestDTO);

    void announcementConfirm(User user, @Valid AnnouncementConfirmRequestDTO announcementRequestDTO);
}