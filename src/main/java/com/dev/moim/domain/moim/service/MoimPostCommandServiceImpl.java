package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.CreateMoimPostDTO;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.repository.MoimPostRepository;
import com.dev.moim.domain.moim.repository.MoimRepository;
import com.dev.moim.domain.moim.repository.UserMoimRepository;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.MoimException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoimPostCommandServiceImpl implements MoimPostCommandService{

    private final MoimRepository moimRepository;
    private final UserMoimRepository userMoimRepository;
    private final MoimPostRepository moimPostRepository;

    @Override
    public Post createMoimPost(User user, CreateMoimPostDTO createMoimPostDTO) {

        Moim moim = moimRepository.findById(createMoimPostDTO.moimId()).orElseThrow(()-> new MoimException(ErrorStatus.MOIM_NOT_FOUND));

        UserMoim userMoim = userMoimRepository.findByUserAndMoim(user, moim).orElseThrow(()-> new MoimException(ErrorStatus.USER_NOT_MOIM_JOIN));

        Post savedPost = Post.builder()
                .title(createMoimPostDTO.title())
                .content(createMoimPostDTO.content())
                .postType(createMoimPostDTO.postType())
                .userMoim(userMoim)
                .moim(moim).build();

        moimPostRepository.save(savedPost);

        return savedPost;
    }
}
