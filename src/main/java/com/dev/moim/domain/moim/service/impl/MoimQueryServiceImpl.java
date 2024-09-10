package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.enums.Gender;
import com.dev.moim.domain.account.repository.UserProfileRepository;
import com.dev.moim.domain.moim.controller.enums.MoimRequestJoin;
import com.dev.moim.domain.moim.controller.enums.MoimRequestRole;
import com.dev.moim.domain.moim.dto.MoimDetailDTO;
import com.dev.moim.domain.moim.dto.MoimIntroduceDTO;
import com.dev.moim.domain.moim.entity.*;
import com.dev.moim.domain.moim.entity.Plan;
import com.dev.moim.domain.moim.dto.*;
import com.dev.moim.domain.moim.entity.*;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.MoimRole;
import com.dev.moim.domain.moim.entity.enums.PostType;
import com.dev.moim.domain.moim.repository.*;
import com.dev.moim.domain.moim.service.impl.dto.IntroduceVideoDTO;
import com.dev.moim.domain.moim.service.impl.dto.JoinRequestDTO;
import com.dev.moim.domain.moim.service.impl.dto.UserProfileDTO;
import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.moim.controller.enums.MoimRequestType;
import com.dev.moim.domain.moim.dto.MoimPreviewDTO;
import com.dev.moim.domain.moim.dto.MoimPreviewListDTO;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.enums.MoimCategory;
import com.dev.moim.domain.moim.service.MoimQueryService;
import com.dev.moim.domain.user.dto.UserPreviewDTO;
import com.dev.moim.domain.user.dto.UserPreviewListDTO;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.MoimException;
import com.dev.moim.global.error.handler.PlanException;
import com.dev.moim.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MoimQueryServiceImpl implements MoimQueryService {

    private final MoimRepository moimRepository;
    private final UserRepository userRepository;
    private final UserMoimRepository userMoimRepository;
    private final PostRepository postRepository;
    private final PlanRepository planRepository;
    private final S3Service s3Service;
    private final UserProfileRepository userProfileRepository;

    @Override
    public MoimPreviewListDTO getUserMoim(Long userId, Long cursor, Integer take, MoimRequestRole moimRequestRole) {

        if (cursor == 1) {
            cursor = Long.MAX_VALUE;
        }

        Slice<Moim> myMoims;
        if (moimRequestRole.equals(MoimRequestRole.ALL)) {
            myMoims = moimRepository.findMyMoims(userId, cursor, PageRequest.of(0, take));
        } else {
            MoimRole moimRole = MoimRole.valueOf(moimRequestRole.toString());
            myMoims = moimRepository.findMyMoimsWithMoimRole(userId, cursor, moimRole, PageRequest.of(0, take));
        }

        List<MoimPreviewDTO> findMyMoims = myMoims.stream().map((moim)->{
            return MoimPreviewDTO.toMoimPreviewDTO(moim, moim.getImageUrl());
        }).toList();

        Long nextCursor = null;
        if (!myMoims.isLast()) {
            nextCursor = myMoims.toList().get(myMoims.toList().size() - 1).getId();
        }

        return MoimPreviewListDTO.toMoimPreviewListDTO(findMyMoims, nextCursor, myMoims.hasNext());
    }

    @Override
    public MoimPreviewListDTO findMoims(List<MoimRequestType> moimRequestTypes, String name, Long cursor, Integer take) {

        if (name == null) {
            name = "";
        }

        if (cursor == 1) {
            cursor = Long.MAX_VALUE;
        }

        Slice<Moim> moimSlice;
        if (moimRequestTypes.isEmpty()) {
            moimSlice = moimRepository.findByNameLikeAndIdLessThanOrderByIdDesc(name, cursor, PageRequest.of(0, take));
        } else {
            List<MoimCategory> moimCategory = moimRequestTypes.stream().map((moimRequestType -> MoimCategory.valueOf(moimRequestType.toString()))).toList();
            moimSlice = moimRepository.findByMoimCategoryAndNameLikeAndIdLessThanOrderByIdDesc(moimCategory, name, cursor, PageRequest.of(0, take));
        }

        Long nextCursor = null;
        if (!moimSlice.isLast()) {
            nextCursor = moimSlice.toList().get(moimSlice.toList().size() - 1).getId();
        }

        List<MoimPreviewDTO> moimPreviewDTOList = moimSlice.toList().stream().map((moim) -> {
            return MoimPreviewDTO.toMoimPreviewDTO(moim, moim.getImageUrl());
        }).toList();

        return MoimPreviewListDTO.toMoimPreviewListDTO(moimPreviewDTOList, nextCursor, moimSlice.hasNext());
    }

    @Override
    public UserPreviewListDTO getMoimMembers(Long moimId, Long cursor, Integer take, String search) {

        Slice<UserProfileDTO> moimUsers = userRepository.findUserByMoimId(moimId, search, JoinStatus.COMPLETE, cursor, PageRequest.of(0, take));

        List<UserPreviewDTO> userPreviewDTOList = moimUsers.toList().stream().map(UserPreviewDTO::toUserPreviewDTO).toList();

        Long nextCursor = null;
        if (!moimUsers.isLast()) {
            nextCursor = moimUsers.toList().get(moimUsers.toList().size() - 1).getUserMoim().getId();
        }

        return UserPreviewListDTO.toUserPreviewListDTO(userPreviewDTOList, moimUsers.hasNext(), nextCursor);
    }

    @Override
    public UserPreviewListDTO getMoimMembersExcludeOwner(Long moimId, Long cursor, Integer take, String search) {

        Slice<UserProfileDTO> moimUsers = userRepository.findUserByMoimIdExcludeOwner(moimId, search, JoinStatus.COMPLETE, cursor, PageRequest.of(0, take));

        List<UserPreviewDTO> userPreviewDTOList = moimUsers.toList().stream().map(UserPreviewDTO::toUserPreviewDTO).toList();

        Long nextCursor = null;
        if (!moimUsers.isLast()) {
            nextCursor = moimUsers.toList().get(moimUsers.toList().size() - 1).getUserMoim().getId();
        }

        return UserPreviewListDTO.toUserPreviewListDTO(userPreviewDTOList, moimUsers.hasNext(), nextCursor);
    }

    @Override
    public UserPreviewListDTO findRequestMember(User user, Long moimId, Long cursor, Integer take, String search) {


        Slice<UserProfileDTO> moimUsers = userRepository.findUserByMoimId(moimId, search, JoinStatus.LOADING, cursor, PageRequest.of(0, take));

        List<UserPreviewDTO> userPreviewDTOList = moimUsers.toList().stream().map(UserPreviewDTO::toUserPreviewDTO).toList();

        Long nextCursor = null;
        if (!moimUsers.isLast()) {
            nextCursor = moimUsers.toList().get(moimUsers.toList().size() - 1).getUserMoim().getId();
        }

        return UserPreviewListDTO.toUserPreviewListDTO(userPreviewDTOList, moimUsers.hasNext(), nextCursor);
    }

    @Override
    public MoimIntroduceDTO getIntroduce(Long moimId) {
        Moim moim = moimRepository.findById(moimId).orElseThrow(() -> new MoimException(ErrorStatus.MOIM_NOT_FOUND));
        IntroduceVideoDTO introduceVideo = userMoimRepository.findIntroduceVideo(moimId).orElseThrow(() -> new MoimException(ErrorStatus.VIDEO_ERROR));
        return MoimIntroduceDTO.toMoimIntroduceDTO(introduceVideo.getMoim(), introduceVideo.getUserProfile());
    }

    @Override
    public MoimPreviewListDTO getPopularMoim() {

        return null;
    }

    @Override
    public MoimPreviewListDTO getNewMoim(Long cursor, Integer take) {

        if (cursor == 1) {
            cursor = Long.MAX_VALUE;
        }

        Slice<Moim> moims = moimRepository.findByIdLessThanOrderByIdDesc(cursor, PageRequest.of(0, take));

        List<MoimPreviewDTO> findMyMoims = moims.stream().map((moim)->{
            return MoimPreviewDTO.toMoimPreviewDTO(moim, moim.getImageUrl());
        }).toList();


        Long nextCursor = null;
        if (!moims.isLast()) {
            nextCursor = moims.toList().get(moims.toList().size() - 1).getId();
        }

        return MoimPreviewListDTO.toMoimPreviewListDTO(findMyMoims, nextCursor, moims.hasNext());
    }

    @Override
    public MoimDetailDTO getMoimDetail(User user, Long moimId) {
        Moim moim = moimRepository.findById(moimId).orElseThrow(() -> new MoimException(ErrorStatus.MOIM_NOT_FOUND));
        int reviewCount = postRepository.findByMoimAndPostType(moim, PostType.REVIEW).size();
        List<User> users = userRepository.findUserByMoim(moim, JoinStatus.COMPLETE);
        List<Plan> moims = planRepository.findByMoim(moim);
        JoinStatus joinStatus = userMoimRepository.findJoinStatusByUserAndMoim(user, moim);
        List<UserProfileDTO> userProfileList = userProfileRepository.findRandomProfile(moim, JoinStatus.COMPLETE, PageRequest.of(0, 3));
        List<UserPreviewDTO> userPreviewDTOList = userProfileList.stream().map(UserPreviewDTO::toUserPreviewDTO).toList();
        Optional<MoimRole> moimRoleByUser = userMoimRepository.findMoimRoleByUserAndMoim(user, moim);

        Double totalAge = 0.0;
        Double averageAge = 0.0;
        Long maleSize = 0L;
        Long femaleSize = 0L;
        Long count = 0L;
        for(User u : users) {
            if (u.getGender().equals(Gender.MALE)) {
                maleSize += 1L;
            } else {
                femaleSize += 1L;
            }
            totalAge += Integer.parseInt(String.valueOf(LocalDate.now().getYear() - u.getBirth().getYear())) + 1;
            count ++;
        }

        if (count > 0) {
            averageAge = totalAge / count;
        }

        MoimRole moimRole;
        if (moimRoleByUser.isPresent()) {
            moimRole = moimRoleByUser.get();
        } else {
            moimRole = null;
        }

        return MoimDetailDTO.toMoimDetailDTO(moim, moimRole, joinStatus, moim.getImageUrl(), averageAge, moims.size(), reviewCount, maleSize, femaleSize, users.size(), userPreviewDTOList);

    }

    @Override
    public Long findMoimOwner(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanException(PLAN_NOT_FOUND));

        UserMoim userMoim = userMoimRepository.findByMoimIdAndMoimRole(plan.getMoim().getId(), MoimRole.OWNER)
                .orElseThrow(() -> new MoimException(MOIM_OWNER_NOT_FOUND));

        return userMoim.getUser().getId();
    }

    @Override
    public MoimJoinRequestListDTO findMyRequestMoims(User user, Long cursor, Integer take, MoimRequestJoin moimRequestJoin) {

        if (cursor == 1) {
            cursor = Long.MAX_VALUE;
        }

        Slice<JoinRequestDTO> joinRequestDTOSlice;
        if (moimRequestJoin.equals(MoimRequestJoin.ALL)) {
            joinRequestDTOSlice = userMoimRepository.findMyRequestMoims(user, cursor, PageRequest.of(0, take));
        } else {
            JoinStatus joinStatus = JoinStatus.valueOf(moimRequestJoin.toString());
            joinRequestDTOSlice = userMoimRepository.findMyRequestMoimsWithJoinStatus(user, cursor, joinStatus, PageRequest.of(0, take));
        }


        List<MoimJoinRequestDTO> moimJoinRequestDTOList = joinRequestDTOSlice.map((j) -> {
                    List<User> users = userRepository.findUserByMoim(j.getMoim(), JoinStatus.COMPLETE);
                    return MoimJoinRequestDTO.toMoimJoinRequestDTO(j, users.size());
                }
        ).toList();

        Long nextCursor = null;
        if (!joinRequestDTOSlice.isLast()) {
            nextCursor = joinRequestDTOSlice.toList().get(joinRequestDTOSlice.toList().size() - 1).getUserMoim().getId();
        }

        return MoimJoinRequestListDTO.toMoimJoinRequestListDTO(moimJoinRequestDTOList, joinRequestDTOSlice.hasNext(), nextCursor);
    }

    @Override
    public boolean existsByMoimId(Long moimId) {
        return moimRepository.existsById(moimId);
    }

    @Override
    public boolean existsByUserIdAndMoimIdAndJoinStatus(Long userId, Long moimId, JoinStatus joinStatus) {
        return userMoimRepository.existsByUserIdAndMoimIdAndJoinStatus(userId, moimId, joinStatus);
    }

    @Override
    public List<Long> findAllMemberIdByMoimId(Long moimId) {
        return userMoimRepository.findByMoimIdAndJoinStatus(moimId, JoinStatus.COMPLETE).stream()
                .map(userMoim -> userMoim.getUser().getId())
                .toList();
    }
}
