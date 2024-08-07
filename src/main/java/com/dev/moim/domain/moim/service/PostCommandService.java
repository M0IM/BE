package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.post.CommentLikeDTO;
import com.dev.moim.domain.moim.dto.post.CreateCommentCommentDTO;
import com.dev.moim.domain.moim.dto.post.CreateCommentDTO;
import com.dev.moim.domain.moim.dto.post.CreateMoimPostDTO;
import com.dev.moim.domain.moim.dto.post.PostLikeDTO;
import com.dev.moim.domain.moim.entity.Comment;
import com.dev.moim.domain.moim.entity.Post;

public interface PostCommandService {
    Post createMoimPost(User user, CreateMoimPostDTO createMoimPostDTO);

    Comment createComment(User user, CreateCommentDTO createCommentDTO);

    Comment createCommentComment(User user, CreateCommentCommentDTO createCommentCommentDTO);

    void postLike(User user, PostLikeDTO postLikeDTO);

    void commentLike(User user, CommentLikeDTO commentLikeDTO);
}
