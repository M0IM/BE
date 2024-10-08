package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.controller.enums.MoimRequestJoin;
import com.dev.moim.domain.moim.controller.enums.MoimRequestRole;
import com.dev.moim.domain.moim.controller.enums.MoimRequestType;
import com.dev.moim.domain.moim.dto.MoimDetailDTO;
import com.dev.moim.domain.moim.dto.MoimIntroduceDTO;
import com.dev.moim.domain.moim.dto.MoimJoinRequestListDTO;
import com.dev.moim.domain.moim.dto.MoimPreviewListDTO;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.MoimRole;
import com.dev.moim.domain.user.dto.UserPreviewListDTO;

import java.util.List;

public interface MoimQueryService {

    MoimPreviewListDTO getUserMoim(Long userId, Long cursor, Integer take, MoimRequestRole moimRequestRole);

    MoimPreviewListDTO findMoims(List<MoimRequestType> moimRequestTypes, String name, Long cursor, Integer take);

    UserPreviewListDTO getMoimMembers(Long moimId, Long cursor, Integer take, String search);

    UserPreviewListDTO getMoimMembersExcludeOwner(Long moimId, Long cursor, Integer take, String search);

    UserPreviewListDTO findRequestMember(User user, Long moimId, Long cursor, Integer take, String search);

    MoimIntroduceDTO getIntroduce(Long moimId);

    MoimPreviewListDTO getPopularMoim();

    MoimPreviewListDTO getNewMoim(Long cursor, Integer take);

    MoimDetailDTO getMoimDetail(User user, Long moimId);

    Long findMoimOwner(Long moimId);

    MoimJoinRequestListDTO findMyRequestMoims(User user, Long cursor, Integer take, MoimRequestJoin moimRequestJoin);

    boolean existsByMoimId(Long moimId);

    boolean existsByUserIdAndMoimIdAndJoinStatus(Long userId, Long moimId, JoinStatus joinStatus);

    List<Long> findAllMemberIdByMoimId(Long moimId);
}
