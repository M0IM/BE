package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.moim.dto.post.*;
import com.dev.moim.domain.moim.entity.*;
import com.dev.moim.domain.moim.repository.*;
import com.dev.moim.domain.moim.service.PostCommandService;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.CommentException;
import com.dev.moim.global.error.handler.MoimException;
import com.dev.moim.global.error.handler.PostException;
import com.dev.moim.global.firebase.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    private final PostReportRepository postReportRepository;
    private final PostBlockRepository postBlockRepository;
    private final UserRepository userRepository;
    private final FcmService fcmService;

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

        if (user.getIsPushAlarm()) {
            fcmService.sendNotification(post.getUserMoim().getUser(), "새로운 댓글이 달렸습니다.", comment.getContent());
        }

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

        if (user.getIsPushAlarm()) {
            fcmService.sendNotification(parentComment.getUserMoim().getUser(), "새로운 대댓글이 달렸습니다.", comment.getContent());
        }

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

    @Override
    public void reportMoimPost(User user, PostReportDTO postReportDTO) {

        Post post = postRepository.findById(postReportDTO.postId()).orElseThrow(() -> new PostException(ErrorStatus.POST_NOT_FOUND));

        Optional<PostReport> postReport = postReportRepository.findByUserAndPost(user, post);

        if (postReport.isPresent()) {

            // 이미 있음.
            postReportRepository.delete(postReport.get());
        } else {

            // 없음 -> 삭제
            PostReport savedPostReport = PostReport.builder()
                    .post(post)
                    .user(user)
                    .build();

            postReportRepository.save(savedPostReport);
        }


    }

    @Override
    public void deletePost(User user, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(ErrorStatus.POST_NOT_FOUND));

        postRepository.delete(post);
    }

    @Override
    public void updatePost(User user, UpdateMoimPostDTO updateMoimPostDTO) {
        Post updatePost = postRepository.findById(updateMoimPostDTO.postId()).orElseThrow(() -> new PostException(ErrorStatus.POST_NOT_FOUND));

        List<PostImage> imageList = updateMoimPostDTO.imageKeyNames().stream().map((i) ->
                PostImage.builder().imageKeyName(i).post(updatePost).build()
        ).toList();

        updatePost.updatePost(updateMoimPostDTO.title(), updateMoimPostDTO.content(), imageList);
    }

    @Override
    public void blockPost(User user, PostBlockDTO postBlockDTO) {
        Post post = postRepository.findById(postBlockDTO.postId()).orElseThrow(()-> new PostException(ErrorStatus.POST_NOT_FOUND));

        Optional<PostBlock> postBlock = postBlockRepository.findByUserIdAndPostId(user.getId(), postBlockDTO.postId());

        if (postBlock.isPresent()) {
            postBlockRepository.delete(postBlock.get());
        } else {
            PostBlock savedPostBlock = PostBlock.builder()
                    .user(user)
                    .post(post)
                    .build();

            postBlockRepository.save(savedPostBlock);
        }
    }

    @Override
    public void deleteComment(User user, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentException(ErrorStatus.COMMENT_NOT_FOUND));
        comment.delete();
    }
}
