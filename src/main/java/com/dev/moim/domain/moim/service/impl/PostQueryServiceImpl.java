package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.account.entity.User;

import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.moim.controller.enums.PostRequestType;
import com.dev.moim.domain.moim.converter.PostConverter;
import com.dev.moim.domain.moim.dto.post.*;
import com.dev.moim.domain.moim.entity.*;
import com.dev.moim.domain.moim.entity.enums.PostType;
import com.dev.moim.domain.moim.repository.*;
import com.dev.moim.domain.moim.service.PostQueryService;

import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.MoimException;
import com.dev.moim.global.error.handler.PostException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;
    private final MoimRepository moimRepository;
    private final UserMoimRepository userMoimRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostBlockRepository postBlockRepository;

    @Override
    public MoimPostPreviewListDTO getMoimPostList(User user, Long moimId, PostRequestType postRequestType, Long cursor, Integer take) {
        Moim moim = moimRepository.findById(moimId).orElseThrow(()-> new MoimException(ErrorStatus.MOIM_NOT_FOUND));

        if (cursor == 1) {
            cursor = Long.MAX_VALUE;
        }

        Slice<Post> postSlices;
        if (postRequestType.equals(PostRequestType.ALL)) {
            postSlices = postRepository.findByMoimAndIdLessThanAndUserPostBlocksNotInOrderByIdDesc(moim, cursor, user, PageRequest.of(0, take));
        } else {
            PostType postType = PostType.valueOf(postRequestType.toString());
            postSlices = postRepository.findByMoimAndPostTypeAndIdLessThanAndUserPostBlocksNotInOrderByIdDesc(moim, postType, cursor, user, PageRequest.of(0, take));
        }

        List<MoimPostPreviewDTO> moimPostPreviewDTOList = postSlices.stream().map((p)->{
            Optional<UserMoim> userMoim = userMoimRepository.findByPost(p);
            return MoimPostPreviewDTO.toMoimPostPreviewDTO(p, userMoim);
        }).toList();

        Long nextCursor = null;
        if (!postSlices.isLast()) {
            nextCursor = postSlices.toList().get(postSlices.toList().size() - 1).getId();
        }

        return PostConverter.toMoimPostPreviewListDTO(moimPostPreviewDTOList, postSlices.hasNext(), nextCursor);
    }

    @Override
    public MoimPostDetailDTO getMoimPost(User user, Long moimId, Long postId) {
        Moim moim = moimRepository.findById(moimId).orElseThrow(()-> new MoimException(ErrorStatus.MOIM_NOT_FOUND));

        Optional<PostBlock> byUserIdAndPostId = postBlockRepository.findByUserIdAndPostId(user.getId(), postId);

        if (byUserIdAndPostId.isPresent()) {
            throw new PostException(ErrorStatus.BLOCK_POST);
        }

        if(!userMoimRepository.existsByUserAndMoim(user, moim)) {
           throw new MoimException(ErrorStatus.USER_NOT_MOIM_JOIN);
       }

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(ErrorStatus.POST_NOT_FOUND));

        Boolean postLike = isPostLike(user.getId(), postId);

        Optional<UserMoim> userMoim = userMoimRepository.findByPost(post);

        return MoimPostDetailDTO.toMoimPostDetailDTO(post, postLike, userMoim);
    }

    @Override
    public Boolean isCommentLike(Long userId, Long commentId) {
        return commentLikeRepository.existsCommentLikeByUserIdAndCommentId(userId, commentId);
    }

    @Override
    public CommentResponseListDTO getcomments(User user, Long moimId, Long postId, Long cursor, Integer take) {

        Post post = postRepository.findById(postId).orElseThrow(()-> new PostException(ErrorStatus.POST_NOT_FOUND));


        Slice<Comment> commentSlices = commentRepository.findByPostAndIdGreaterThanAndParentIsNullOrderByIdAsc(post, cursor, PageRequest.of(0, take));

        List<Comment> commentBlockList = commentRepository.findByUserAndPostId(user, postId);

        Long nextCursor = null;
        if (!commentSlices.isLast()) {
            nextCursor = commentSlices.toList().get(commentSlices.toList().size() - 1).getId();
        }

        List<CommentResponseDTO> commentResponseDTOList = commentSlices.stream().map((comment) -> {
            Optional<UserMoim> commentUserMoim = userMoimRepository.findByComment(comment);
            List<CommentCommentResponseDTO> commentCommentResponseDTOList = comment.getChildren().stream().map(commentcomment -> {
                Optional<UserMoim> commentCommentUserMoim = userMoimRepository.findByComment(commentcomment);
                return CommentCommentResponseDTO.toCommentCommentResponseDTO(commentcomment, isCommentLike(user.getId(), commentcomment.getId()), commentBlockList, commentCommentUserMoim);
            }).toList();
            return CommentResponseDTO.toCommentResponseDTO(comment, isCommentLike(user.getId(), comment.getId()), commentCommentResponseDTOList, commentBlockList, commentUserMoim);
        }).toList();

        return CommentResponseListDTO.toCommentResponseListDTO(commentResponseDTOList, nextCursor, commentSlices.hasNext());
    }

    @Override
    public Boolean isPostLike(Long userId, Long postId) {
        return postLikeRepository.existsPostLikeByUserIdAndPostId(userId, postId);
    }

    @Override
    public MoimPostPreviewListDTO getIntroductionPosts(Long cursor, Integer take) {
        if (cursor == 1) {
            cursor = Long.MAX_VALUE;
        }

        Slice<Post> postSlices = postRepository.findByPostTypeAndIdLessThanOrderByIdDesc(PostType.GLOBAL, cursor, PageRequest.of(0, take));

        List<MoimPostPreviewDTO> moimPostPreviewDTOList = postSlices.stream().map((p)->{
            Optional<UserMoim> userMoim = userMoimRepository.findByPost(p);
            return MoimPostPreviewDTO.toMoimPostPreviewDTO(p, userMoim);
        }).toList();

        Long nextCursor = null;
        if (!postSlices.isLast()) {
            nextCursor = postSlices.toList().get(postSlices.toList().size() - 1).getId();
        }

        return PostConverter.toMoimPostPreviewListDTO(moimPostPreviewDTOList, postSlices.hasNext(), nextCursor);
    }

    @Override
    public Post getIntroductionPost(Long postId) {

        return postRepository.findById(postId).orElseThrow(()-> new PostException(ErrorStatus.POST_NOT_FOUND));
    }

    @Override
    public List<JoinMoimPostsResponseDTO> getPostsByJoinMoims(User user) {
        List<Moim> moimsByUser = moimRepository.findMoimsByUser(user);

        List<JoinMoimPostsResponseDTO> joinMoimPostsResponseDTOList = moimsByUser.stream().map((m) -> {
            List<Post> postList = postRepository.findByNotPostTypeAndMoimOrderByCreatedAtDesc(PostType.GLOBAL ,m, PageRequest.of(0, 3));
            List<MoimPostPreviewDTO> moimPostPreviewDTOStream = postList.stream().map((p)->{
                Optional<UserMoim> userMoim = userMoimRepository.findByPost(p);
                return MoimPostPreviewDTO.toMoimPostPreviewDTO(p, userMoim);
            }).toList();

            return JoinMoimPostsResponseDTO.toJoinMoimPostsResponseDTO(m.getId(), m.getName(), moimPostPreviewDTOStream);
        }).toList();

        return joinMoimPostsResponseDTOList;
    }

    @Override
    public List<BlockCommentResponse> findBlockComments(User user) {

        List<Comment> comments = commentRepository.findByBlockComment(user);

        return comments.stream().map(BlockCommentResponse::toBlockCommentResponse).toList();
    }

    @Override
    public List<MoimPostPreviewDTO> findBlockPosts(User user) {

        List<Post> postList = postRepository.findBlockPost(user);

        return postList.stream().map((p)->{
            Optional<UserMoim> userMoim = userMoimRepository.findByPost(p);
            return MoimPostPreviewDTO.toMoimPostPreviewDTO(p, userMoim);
        }).toList();
    }
}
