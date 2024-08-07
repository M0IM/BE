package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.post.CommentLikeDTO;
import com.dev.moim.domain.moim.dto.post.CreateCommentCommentDTO;
import com.dev.moim.domain.moim.dto.post.CreateCommentDTO;
import com.dev.moim.domain.moim.dto.post.CreateMoimPostDTO;
import com.dev.moim.domain.moim.dto.post.PostLikeDTO;
import com.dev.moim.domain.moim.entity.Comment;
import com.dev.moim.domain.moim.entity.CommentLike;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.entity.PostImage;
import com.dev.moim.domain.moim.entity.PostLike;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.repository.CommentLikeRepository;
import com.dev.moim.domain.moim.repository.CommentRepository;
import com.dev.moim.domain.moim.repository.PostLikeRepository;
import com.dev.moim.domain.moim.repository.PostRepository;
import com.dev.moim.domain.moim.repository.MoimRepository;
import com.dev.moim.domain.moim.repository.PostImageRepository;
import com.dev.moim.domain.moim.repository.UserMoimRepository;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.CommentException;
import com.dev.moim.global.error.handler.MoimException;
import com.dev.moim.global.error.handler.PostException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostCommandServiceImpl implements PostCommandService {

    private final MoimRepository moimRepository;
    private final UserMoimRepository userMoimRepository;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Override
    public Post createMoimPost(User user, CreateMoimPostDTO createMoimPostDTO) {

        Moim moim = moimRepository.findById(createMoimPostDTO.moimId()).orElseThrow(()-> new MoimException(ErrorStatus.MOIM_NOT_FOUND));

        UserMoim userMoim = userMoimRepository.findByUserAndMoim(user, moim).orElseThrow(()-> new MoimException(ErrorStatus.USER_NOT_MOIM_JOIN));

        Post savedPost = Post.builder()
                .title(createMoimPostDTO.title())
                .content(createMoimPostDTO.content())
                .postType(createMoimPostDTO.postType())
                .userMoim(userMoim)
                .moim(moim)
                .build();

        postRepository.save(savedPost);

        createMoimPostDTO.imageKeyNames().forEach((i) ->{
                PostImage postImage = PostImage.builder().imageKeyName(i).post(savedPost).build();
                postImageRepository.save(postImage);
            }
        );

        return savedPost;
    }

    @Override
    public Comment createComment(User user, CreateCommentDTO createCommentDTO) {

        Moim moim = moimRepository.findById(createCommentDTO.moimId()).orElseThrow(()-> new MoimException(ErrorStatus.MOIM_NOT_FOUND));

        UserMoim userMoim = userMoimRepository.findByUserAndMoim(user, moim).orElseThrow(()-> new MoimException(ErrorStatus.USER_NOT_MOIM_JOIN));

        Post post = postRepository.findById(createCommentDTO.postId()).orElseThrow(()-> new PostException(ErrorStatus.POST_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(createCommentDTO.content())
                .post(post)
                .userMoim(userMoim)
                .build();

        commentRepository.save(comment);

        return comment;
    }

    @Override
    public Comment createCommentComment(User user, CreateCommentCommentDTO createCommentCommentDTO) {
        Moim moim = moimRepository.findById(createCommentCommentDTO.moimId()).orElseThrow(()-> new MoimException(ErrorStatus.MOIM_NOT_FOUND));

        UserMoim userMoim = userMoimRepository.findByUserAndMoim(user, moim).orElseThrow(()-> new MoimException(ErrorStatus.USER_NOT_MOIM_JOIN));

        Post post = postRepository.findById(createCommentCommentDTO.postId()).orElseThrow(()-> new PostException(ErrorStatus.POST_NOT_FOUND));

        Comment parentComment = commentRepository.findById(createCommentCommentDTO.commentId()).orElseThrow(()-> new CommentException(ErrorStatus.COMMENT_NOT_FOUND));

        Comment comment = Comment.builder()
                .parent(parentComment)
                .content(createCommentCommentDTO.content())
                .post(post)
                .userMoim(userMoim)
                .build();

        commentRepository.save(comment);

        return comment;
    }

    @Override
    public void postLike(User user, PostLikeDTO postLikeDTO) {
        Post post = postRepository.findById(postLikeDTO.postId()).orElseThrow(()-> new PostException(ErrorStatus.POST_NOT_FOUND));

        Optional<PostLike> postLike = postLikeRepository.findByUserIdAndPostId(user.getId(), postLikeDTO.postId());

        if (postLike.isPresent()) {
            postLikeRepository.delete(postLike.get());
        } else {
            PostLike savedPostLike = PostLike.builder()
                    .user(user)
                    .post(post)
                    .build();

            postLikeRepository.save(savedPostLike);
        }
    }

    @Override
    public void commentLike(User user, CommentLikeDTO commentLikeDTO) {
        Comment comment = commentRepository.findById(commentLikeDTO.commentId()).orElseThrow(()-> new CommentException(ErrorStatus.COMMENT_NOT_FOUND));

        Optional<CommentLike> commentLike = commentLikeRepository.findCommentLikeByUserIdAndCommentId(user.getId(), commentLikeDTO.commentId());

        if (commentLike.isPresent()) {
            commentLikeRepository.delete(commentLike.get());
        } else {
            CommentLike savedCommentLike = CommentLike.builder()
                    .comment(comment)
                    .user(user)
                    .build();

            commentLikeRepository.save(savedCommentLike);
        }
    }
}
