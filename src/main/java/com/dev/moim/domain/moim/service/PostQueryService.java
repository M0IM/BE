package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.controller.enums.PostRequestType;
import com.dev.moim.domain.moim.dto.post.CommentResponseDTO;
import com.dev.moim.domain.moim.dto.post.CommentResponseListDTO;
import com.dev.moim.domain.moim.dto.post.MoimPostPreviewListDTO;
import com.dev.moim.domain.moim.entity.Post;
import org.springframework.data.domain.Slice;

public interface PostQueryService {
    MoimPostPreviewListDTO getMoimPostList(User user, Long moimId, PostRequestType postRequestType, Long cursor, Integer take);

    Post getMoimPost(User user, Long moimId, Long postId);

    Boolean isCommentLike(Long userId, Long commentId);

    CommentResponseListDTO getcomments(User user, Long moimId, Long postId, Long cursor, Integer take);

    Boolean isPostLike(Long userId, Long postId);
}
