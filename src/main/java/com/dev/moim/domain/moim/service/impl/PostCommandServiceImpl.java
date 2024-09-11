package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.enums.AlarmDetailType;
import com.dev.moim.domain.account.entity.enums.AlarmType;
import com.dev.moim.domain.account.repository.AlarmRepository;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.account.service.AlarmService;
import com.dev.moim.domain.moim.dto.post.*;
import com.dev.moim.domain.moim.entity.*;
import com.dev.moim.domain.moim.entity.enums.CommentStatus;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.PostType;
import com.dev.moim.domain.moim.repository.*;
import com.dev.moim.domain.moim.service.PostCommandService;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.CommentException;
import com.dev.moim.global.error.handler.MoimException;
import com.dev.moim.global.error.handler.PostException;
import com.dev.moim.global.firebase.service.FcmService;
import com.dev.moim.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
    private final FcmService fcmService;
    private final CommentReportRepository commentReportRepository;
    private final CommentBlockRepository commentBlockRepository;
    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final AlarmService alarmService;
    private final ReadPostRepository readPostRepository;

    @Override
    public Post createMoimPost(User user, CreateMoimPostDTO createMoimPostDTO) {

        if (createMoimPostDTO.postType().equals(PostType.ANNOUNCEMENT)) {
            throw new PostException(ErrorStatus._FORBIDDEN);
        }

        Moim moim = moimRepository.findById(createMoimPostDTO.moimId()).orElseThrow(()-> new MoimException(ErrorStatus.MOIM_NOT_FOUND));

        UserMoim userMoim = userMoimRepository.findByUserIdAndMoimId(user.getId(), moim.getId(), JoinStatus.COMPLETE).orElseThrow(()-> new MoimException(ErrorStatus.USER_NOT_MOIM_JOIN));

        Post savedPost = Post.builder()
                .title(createMoimPostDTO.title())
                .content(createMoimPostDTO.content())
                .postType(createMoimPostDTO.postType())
                .userMoim(userMoim)
                .moim(moim)
                .build();

        postRepository.save(savedPost);

        createMoimPostDTO.imageKeyNames().forEach((i) ->{
                PostImage postImage = PostImage.builder().imageKeyName(i == null || i.isEmpty() || i.isBlank() ? null : s3Service.generateStaticUrl(i)).post(savedPost).build();
                postImageRepository.save(postImage);
            }
        );

        return savedPost;
    }

    @Override
    public Comment createComment(User user, CreateCommentDTO createCommentDTO) {

        Moim moim = moimRepository.findById(createCommentDTO.moimId()).orElseThrow(()-> new MoimException(ErrorStatus.MOIM_NOT_FOUND));

        UserMoim userMoim = userMoimRepository.findByUserIdAndMoimId(user.getId(), moim.getId(), JoinStatus.COMPLETE).orElseThrow(()-> new MoimException(ErrorStatus.USER_NOT_MOIM_JOIN));

        Post post = postRepository.findById(createCommentDTO.postId()).orElseThrow(()-> new PostException(ErrorStatus.POST_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(createCommentDTO.content())
                .post(post)
                .commentStatus(CommentStatus.ACTIVE)
                .userMoim(userMoim)
                .build();

        commentRepository.save(comment);

        if (post.getUserMoim().getUser().getIsPushAlarm() && post.getUserMoim().getUser() != user) {
            alarmService.saveAlarm(user, post.getUserMoim().getUser(), "새로운 댓글이 달렸습니다.", comment.getContent(), AlarmType.PUSH, AlarmDetailType.COMMENT, moim.getId(), post.getId(), comment.getId());
            fcmService.sendPushNotification(post.getUserMoim().getUser(), "새로운 댓글이 달렸습니다.", comment.getContent(), AlarmDetailType.COMMENT);
        }

        return comment;
    }

    @Override
    public Comment createCommentComment(User user, CreateCommentCommentDTO createCommentCommentDTO) {
        Moim moim = moimRepository.findById(createCommentCommentDTO.moimId()).orElseThrow(()-> new MoimException(ErrorStatus.MOIM_NOT_FOUND));

        UserMoim userMoim = userMoimRepository.findByUserIdAndMoimId(user.getId(), moim.getId(), JoinStatus.COMPLETE).orElseThrow(()-> new MoimException(ErrorStatus.USER_NOT_MOIM_JOIN));

        Post post = postRepository.findById(createCommentCommentDTO.postId()).orElseThrow(()-> new PostException(ErrorStatus.POST_NOT_FOUND));

        Comment parentComment = commentRepository.findById(createCommentCommentDTO.commentId()).orElseThrow(()-> new CommentException(ErrorStatus.COMMENT_NOT_FOUND));

        Comment comment = Comment.builder()
                .parent(parentComment)
                .content(createCommentCommentDTO.content())
                .post(post)
                .commentStatus(CommentStatus.ACTIVE)
                .userMoim(userMoim)
                .build();

        commentRepository.save(comment);

        if (parentComment.getUserMoim().getUser().getIsPushAlarm() && parentComment.getUserMoim().getUser() != user) {
            alarmService.saveAlarm(user, parentComment.getUserMoim().getUser(), "새로운 대댓글이 달렸습니다.", comment.getContent(), AlarmType.PUSH, AlarmDetailType.COMMENT, moim.getId(), post.getId(), comment.getId());
            fcmService.sendPushNotification(parentComment.getUserMoim().getUser(), "새로운 대댓글이 달렸습니다.", comment.getContent(), AlarmDetailType.COMMENT);
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

            Optional<UserMoim> postByUserMoim = userMoimRepository.findByPost(post);

            if (postByUserMoim.isPresent() && post.getUserMoim().getUser().getIsPushAlarm() && post.getUserMoim().getUser() != user) {
                alarmService.saveAlarm(user, post.getUserMoim().getUser(), "좋아요가 달렸습니다", post.getTitle()+"에 좋아요가 달렸습니다", AlarmType.PUSH, AlarmDetailType.POST, post.getMoim().getId(), post.getId(), null);
                fcmService.sendPushNotification(post.getUserMoim().getUser(), "좋아요가 달렸습니다.", post.getTitle()+"에 좋아요가 달렸습니다", AlarmDetailType.POST);
            }
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

            Optional<UserMoim> userMoimByComment = userMoimRepository.findByComment(comment);

            if (userMoimByComment.isPresent() && comment.getUserMoim().getUser().getIsPushAlarm() && comment.getUserMoim().getUser() != user) {
                alarmService.saveAlarm(user, comment.getUserMoim().getUser(), "좋아요가 달렸습니다", comment.getContent()+"에 좋아요가 달렸습니다", AlarmType.PUSH, AlarmDetailType.COMMENT, comment.getUserMoim().getMoim().getId(), comment.getId(), comment.getId());
                fcmService.sendPushNotification(comment.getUserMoim().getUser(), "좋아요가 달렸습니다.", comment.getContent()+"에 좋아요가 달렸습니다", AlarmDetailType.COMMENT);
            }
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
    public Post updatePost(User user, UpdateMoimPostDTO updateMoimPostDTO) {
        Post updatePost = postRepository.findById(updateMoimPostDTO.postId())
                .orElseThrow(() -> new PostException(ErrorStatus.POST_NOT_FOUND));

        // imageKeyNames()의 반환값이 null인지 체크
        List<String> imageKeyNames = updateMoimPostDTO.imageKeyNames();
        if (imageKeyNames == null) {
            imageKeyNames = Collections.emptyList(); // 빈 리스트로 초기화
        }

        List<PostImage> imageList = imageKeyNames.stream().map(i ->
                PostImage.builder()
                        .imageKeyName(i == null || i.isEmpty() || i.isBlank() ? null : s3Service.generateStaticUrl(i))
                        .post(updatePost)
                        .build()
        ).toList();

        updatePost.updatePost(updateMoimPostDTO.title(), updateMoimPostDTO.content(), imageList);

        return updatePost;
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
        if (!user.equals(comment.getUserMoim().getUser())){
            throw new PostException(ErrorStatus.NOT_MY_POST);
        }


        if (comment.getParent() != null) {
            // 대댓 일시
            commentRepository.delete(comment);
        } else {
            // 댓글 일시
            if (comment.getChildren().isEmpty()) {
                // 자식이 없는 댓글일 시
                commentRepository.delete(comment);
            }
        }

        comment.delete();
    }

    @Override
    public void updateComment(User user, CommentUpdateRequestDTO commentUpdateRequestDTO) {
        Comment comment = commentRepository.findById(commentUpdateRequestDTO.commentId()).orElseThrow(() -> new CommentException(ErrorStatus.COMMENT_NOT_FOUND));

        User user2 = null;
        try {
            user2 = comment.getUserMoim().getUser();
        } catch (Exception e) {
            throw new PostException(ErrorStatus.ALREADY_COMMENT_DELETE);
        }

        if (!user.equals(user2)){
            throw new PostException(ErrorStatus.NOT_MY_POST);
        }

        comment.update(commentUpdateRequestDTO.content());
    }

    @Override
    public void reportComment(User user, CommentReportDTO commentReportDTO) {
        Comment comment = commentRepository.findById(commentReportDTO.commentId()).orElseThrow(() -> new CommentException(ErrorStatus.COMMENT_NOT_FOUND));

        if (!comment.getPost().getId().equals(commentReportDTO.postId())) {
            throw new PostException(ErrorStatus.NOT_INCLUDE_POST);
        }

        Optional<CommentReport> commentReport = commentReportRepository.findByUserAndComment(user, comment);

        if (commentReport.isPresent()) {
            // 이미 있음.
            commentReportRepository.delete(commentReport.get());
        } else {

            // 없음 -> 삭제
            CommentReport savedPostReport = CommentReport.builder()
                    .comment(comment)
                    .user(user)
                    .build();

            commentReportRepository.save(savedPostReport);
        }
    }

    @Override
    public void blockComment(User user, CommentBlockDTO commentBlockDTO) {
        Comment comment = commentRepository.findById(commentBlockDTO.commentId()).orElseThrow(() -> new CommentException(ErrorStatus.COMMENT_NOT_FOUND));

        if (!comment.getPost().getId().equals(commentBlockDTO.postId())) {
            throw new PostException(ErrorStatus.NOT_INCLUDE_POST);
        }

        Optional<CommentBlock> commentBlock = commentBlockRepository.findByUserAndComment(user, comment);

        if (commentBlock.isPresent()) {
            // 이미 있음.
            commentBlockRepository.delete(commentBlock.get());
        } else {

            // 없음 -> 삭제
            CommentBlock savedCommentBlock = CommentBlock.builder()
                    .comment(comment)
                    .user(user)
                    .build();

            commentBlockRepository.save(savedCommentBlock);
        }
    }

    @Override
    public Long createAnnouncement(User user, AnnouncementRequestDTO announcementRequestDTO) {
        Moim moim = moimRepository.findById(announcementRequestDTO.moimId()).orElseThrow(()-> new MoimException(ErrorStatus.MOIM_NOT_FOUND));

        UserMoim userMoim = userMoimRepository.findByUserIdAndMoimId(user.getId(), moim.getId(), JoinStatus.COMPLETE).orElseThrow(()-> new MoimException(ErrorStatus.USER_NOT_MOIM_JOIN));

        Post savedPost = Post.builder()
                .title(announcementRequestDTO.title())
                .content(announcementRequestDTO.content())
                .postType(PostType.ANNOUNCEMENT)
                .userMoim(userMoim)
                .moim(moim)
                .build();

        postRepository.save(savedPost);

        announcementRequestDTO.imageKeyNames().forEach((i) ->{
                    PostImage postImage = PostImage.builder().imageKeyName(i == null || i.isEmpty() || i.isBlank() ? null : s3Service.generateStaticUrl(i)).post(savedPost).build();
                    postImageRepository.save(postImage);
                }
        );

        List<Long> unReadUserIds;
        if (announcementRequestDTO.isAllUserSelected()) {
           unReadUserIds = userRepository.findUserByMoim(moim, JoinStatus.COMPLETE).stream().map(User::getId).toList();
        } else {
            unReadUserIds = announcementRequestDTO.userIds();
        }


        List<User> allById = userRepository.findAllById(unReadUserIds);
        List<ReadPost> readPosts = allById.stream().map((u) ->
                ReadPost.builder().post(savedPost).user(u).isRead(false).build()
        ).toList();

        readPostRepository.saveAll(readPosts);


        List<User> userByMoim = userRepository.findUserByMoim(moim, JoinStatus.COMPLETE);
        if (savedPost.getPostType().equals(PostType.ANNOUNCEMENT)) {
            String name = moim.getName();
            String moimName = (name != null && name.length() >= 7)
                    ? name.substring(0, 7)
                    : (name != null ? name : "");
            userByMoim.forEach((u) ->{
                if (u.getIsPushAlarm() && u != user) {
                    alarmService.saveAlarm(user, u, "[" + moimName  +"] 새로운 공지사항이 있습니다.", savedPost.getTitle(), AlarmType.PUSH, AlarmDetailType.POST, moim.getId(), savedPost.getId(), null);
                    fcmService.sendPushNotification(u, "[" + moimName  +"] 새로운 공지사항이 있습니다.", savedPost.getTitle(), AlarmDetailType.POST);
                }
            });
        }

        return savedPost.getId();
    }

    @Override
    public void announcementConfirm(User user, AnnouncementConfirmRequestDTO announcementRequestDTO) {
        Post post = postRepository.findById(announcementRequestDTO.postId()).orElseThrow(()-> new PostException(ErrorStatus.POST_NOT_FOUND));

        ReadPost readPost = readPostRepository.findByUserAndPost(user, post).orElseThrow(() -> new PostException(ErrorStatus._FORBIDDEN));
        readPost.read();
    }
}
