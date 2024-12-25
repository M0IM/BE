package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.moim.*;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.UserMoim;
import jakarta.validation.Valid;

public interface MoimCommandService {
    Moim createMoim(User user, CreateMoimDTO createMoimDTO);

    void withDrawMoim(User user, @Valid WithMoimDTO withMoimDTO);

    void modifyMoimInfo(@Valid UpdateMoimDTO updateMoimDTO);

    void joinMoim(User user, Long moimId);

    void acceptMoim(UserMoim userMoim, MoimJoinConfirmRequestDTO moimJoinConfirmRequestDTO);

    ChangeAuthorityResponseDTO changeMemberAuthorities(UserMoim userMoim, ChangeAuthorityRequestDTO changeAuthorityRequestDTO);

    void rejectMoims(MoimJoinConfirmRequestDTO moimJoinConfirmRequestDTO);

    void changeMoimLeader(User user, @Valid ChangeMoimLeaderRequestDTO changeMoimLeaderRequestDTO);

    void findMyRequestMoimsConfirm(User user, Long moimId);

    void moimExpel(User user, Long userId, Long moimId);

    MoimRoleResponse moimsMyRole(User user, Long moimId);
}
