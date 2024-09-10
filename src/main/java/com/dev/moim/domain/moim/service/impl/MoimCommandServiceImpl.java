package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.enums.AlarmDetailType;
import com.dev.moim.domain.account.entity.enums.AlarmType;
import com.dev.moim.domain.account.entity.enums.ProfileType;
import com.dev.moim.domain.account.repository.UserProfileRepository;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.account.service.AlarmService;
import com.dev.moim.domain.moim.dto.*;
import com.dev.moim.domain.moim.entity.*;
import com.dev.moim.domain.moim.entity.ExitReason;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.MoimRole;
import com.dev.moim.domain.moim.entity.enums.PostType;
import com.dev.moim.domain.moim.entity.enums.ProfileStatus;
import com.dev.moim.domain.moim.repository.*;
import com.dev.moim.domain.moim.repository.ExitReasonRepository;
import com.dev.moim.domain.moim.repository.MoimRepository;
import com.dev.moim.domain.moim.repository.UserMoimRepository;
import com.dev.moim.domain.moim.service.MoimCommandService;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.MoimException;
import com.dev.moim.global.error.handler.UserException;
import com.dev.moim.global.firebase.service.FcmService;
import com.dev.moim.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MoimCommandServiceImpl implements MoimCommandService {

    private final MoimRepository moimRepository;
    private final UserMoimRepository userMoimRepository;
    private final ExitReasonRepository exitReasonRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final FcmService fcmService;
    private final AlarmService alarmService;
    private final PostRepository postRepository;

    @Override
    public Moim createMoim(User user, CreateMoimDTO createMoimDTO) {


        Moim moim = Moim.builder()
                .name(createMoimDTO.title())
                .introduction(createMoimDTO.introduction())
                .location(createMoimDTO.location())
                .moimCategory(createMoimDTO.moimCategory())
                .introduceVideoKeyName(imageNullProcess(createMoimDTO.introduceVideoKeyName()))
                .introduceVideoTitle(createMoimDTO.introduceVideoTitle())
                .imageUrl(imageNullProcess(createMoimDTO.imageKeyName()))
                .build();

        moimRepository.save(moim);

        UserProfile userProfile = userProfileRepository.findByUserIdAndProfileType(user.getId(), ProfileType.MAIN).orElseThrow(()-> new MoimException(ErrorStatus.USER_PROFILE_NOT_FOUND_MAIN));


        UserMoim userMoim = UserMoim.builder()
                .moim(moim)
                .user(user)
                .moimRole(MoimRole.OWNER)
                .joinStatus(JoinStatus.COMPLETE)
                .profileStatus(ProfileStatus.PRIVATE)
                .userProfile(userProfile)
                .confirm(true)
                .build();

        userMoimRepository.save(userMoim);

        Post savedPost = Post.builder()
                .title(createMoimDTO.title())
                .content(createMoimDTO.introduction())
                .postType(PostType.GLOBAL)
                .userMoim(userMoim)
                .moim(moim)
                .build();

        postRepository.save(savedPost);

        return moim;
    }

    @Override
    public void withDrawMoim(User user, WithMoimDTO withMoimDTO) {
        Moim moim = moimRepository.findById(withMoimDTO.moimId()).orElseThrow(()-> new MoimException(ErrorStatus.MOIM_NOT_FOUND));

        ExitReason exitReason = ExitReason.builder().user(user).moim(moim).content(withMoimDTO.exitReason()).build();

        exitReasonRepository.save(exitReason);

        UserMoim userMoim = userMoimRepository.findByUserIdAndMoimId(user.getId(), moim.getId(), JoinStatus.COMPLETE).orElseThrow(() -> new MoimException(ErrorStatus.USER_NOT_MOIM_JOIN));

        List<User> users = userRepository.findUserByMoim(moim, JoinStatus.COMPLETE);

        if (users.size() > 1 && userMoim.getMoimRole().equals(MoimRole.OWNER)) {
            throw new MoimException(ErrorStatus.OWNER_NOT_EXIT);
        }

        userMoimRepository.delete(userMoim);

        Optional<UserMoim> ownerByMoim = userRepository.findOwnerByMoim(moim);
        if (ownerByMoim.isPresent()) {
            UserMoim owner = ownerByMoim.get();
            if (owner.getUser().getIsPushAlarm() && (user != owner.getUser())) {
                alarmService.saveAlarm(user, owner.getUser(), owner.getUserProfile().getName()+"님이 모임을 탈퇴하셨습니다.", owner.getUserProfile().getName()+"님이 모임을 탈퇴하셨습니다.", AlarmType.PUSH, AlarmDetailType.MOIM, moim.getId(), null, null);
                fcmService.sendPushNotification(owner.getUser(), owner.getUserProfile().getName()+"님이 모임을 탈퇴하셨습니다.", owner.getUserProfile().getName()+"님이 모임을 탈퇴하셨습니다.", AlarmDetailType.MOIM);
            }
        }

        userMoimRepository.flush();

        List<UserMoim> byUserIdAndMoimId = userMoimRepository.findByMoimId(moim.getId(), JoinStatus.COMPLETE);

        if (byUserIdAndMoimId.isEmpty()) {
            moimRepository.delete(moim);
        }
    }

    @Override
    public void modifyMoimInfo(UpdateMoimDTO updateMoimDTO) {
        Moim moim = moimRepository.findById(updateMoimDTO.moimId()).orElseThrow(()-> new MoimException(ErrorStatus.MOIM_NOT_FOUND));

        moim.updateMoim(updateMoimDTO.title(), updateMoimDTO.moimCategory(), updateMoimDTO.address(), updateMoimDTO.description(), imageNullProcess(updateMoimDTO.imageKeyName()));
    }

    @Override
    public void joinMoim(User user, Long moimId) {
        Moim moim = moimRepository.findById(moimId).orElseThrow(() -> new MoimException(ErrorStatus.MOIM_NOT_FOUND));

        Boolean isRequest = userMoimRepository.findByUserAndMoimAndJoinRequest(user, moim, List.of(JoinStatus.LOADING, JoinStatus.COMPLETE));

        if (isRequest) {
            throw new MoimException(ErrorStatus.ALREADY_REQUEST);
        }

        UserProfile userProfile = userProfileRepository.findByUserIdAndProfileType(user.getId(), ProfileType.MAIN).orElseThrow(()-> new MoimException(ErrorStatus.USER_PROFILE_NOT_FOUND_MAIN));
        UserMoim userMoim = UserMoim.builder()
                            .userProfile(userProfile)
                            .joinStatus(JoinStatus.LOADING)
                            .user(user)
                            .moimRole(MoimRole.MEMBER)
                            .moim(moim)
                            .profileStatus(ProfileStatus.PRIVATE)
                            .confirm(false)
                            .build();

        Optional<UserMoim> owner = userRepository.findOwnerByMoim(moim);

        if (owner.isPresent()) {
            User realOwner = owner.get().getUser();
            if (realOwner.getIsPushAlarm() && user != realOwner) {
                alarmService.saveAlarm(user, realOwner, "모임 가입 신청이 들어왔습니다.", "["+moim.getName()+"]에 모임 가입 신청이 들어왔습니다.", AlarmType.PUSH, AlarmDetailType.MOIM, moim.getId(), null, null);
                fcmService.sendPushNotification(realOwner, "모임 가입 신청이 들어왔습니다.", "["+moim.getName()+"]에 모임 가입 신청이 들어왔습니다.", AlarmDetailType.MOIM);
            }
        }

        userMoimRepository.save(userMoim);
    }

    @Override
    public void acceptMoim(User owner, MoimJoinConfirmRequestDTO moimJoinConfirmRequestDTO) {
        User user = userRepository.findById(moimJoinConfirmRequestDTO.userId()).orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));


        Moim moim = moimRepository.findById(moimJoinConfirmRequestDTO.moimId()).orElseThrow(() -> new MoimException(ErrorStatus.MOIM_NOT_FOUND));
        UserMoim userMoim = userMoimRepository.findByUserIdAndMoimId(user.getId(), moim.getId(), JoinStatus.LOADING).orElseThrow(() -> new MoimException(ErrorStatus.NOT_REQUEST_JOIN));

        userMoim.accept();
        userMoim.confirm();


        List<User> admins = userRepository.findAdmins(moim);

        if (user.getIsPushAlarm() && user != owner) {
            alarmService.saveAlarm(owner, user, moim.getName() + " 모임에 가입되었습니다", moim.getName() + "에 가입되었습니다", AlarmType.PUSH, AlarmDetailType.MOIM, moim.getId(), null, null);
            fcmService.sendPushNotification(user,  moim.getName() + " 모임에 가입되었습니다", moim.getName() + "에 가입되었습니다", AlarmDetailType.MOIM);
        }

        admins.stream().filter(admin -> !admin.equals(owner) && admin.getIsPushAlarm()).forEach(admin -> {
            alarmService.saveAlarm(owner, admin, moim.getName() + " 모임에 참여되었습니다", moim.getName() + "에 참여되었습니다", AlarmType.PUSH, AlarmDetailType.MOIM, moim.getId(), null, null);
            fcmService.sendPushNotification(admin,  moim.getName() + " 모임에 참여하었습니다", moim.getName() + "에 참여하었습니다", AlarmDetailType.MOIM);
        });
    }

    @Override
    public ChangeAuthorityResponseDTO changeMemberAuthorities(User user, ChangeAuthorityRequestDTO changeAuthorityRequestDTO) {
        User targetUser = userRepository.findById(changeAuthorityRequestDTO.userId()).orElseThrow(() -> {
            throw new UserException(ErrorStatus.USER_NOT_FOUND);
        });
        Moim moim = moimRepository.findById(changeAuthorityRequestDTO.moimId()).orElseThrow(() -> new MoimException(ErrorStatus.MOIM_NOT_FOUND));
        UserMoim userMoim = userMoimRepository.findByUserIdAndMoimId(targetUser.getId(), moim.getId(), JoinStatus.COMPLETE).orElseThrow(() -> new MoimException(ErrorStatus.USER_NOT_MOIM_JOIN));

        userMoim.changeStatus(changeAuthorityRequestDTO.moimRole());

        return new ChangeAuthorityResponseDTO(targetUser.getId(), userMoim.getMoimRole());
    }

    @Override
    public void rejectMoims(MoimJoinConfirmRequestDTO moimJoinConfirmRequestDTO) {
        User user = userRepository.findById(moimJoinConfirmRequestDTO.userId()).orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));

        Moim moim = moimRepository.findById(moimJoinConfirmRequestDTO.moimId()).orElseThrow(() -> new MoimException(ErrorStatus.MOIM_NOT_FOUND));
        UserMoim userMoim = userMoimRepository.findByUserIdAndMoimId(user.getId(), moim.getId(), JoinStatus.LOADING).orElseThrow(() -> new MoimException(ErrorStatus.NOT_REQUEST_JOIN));

        userMoim.reject();

        Optional<User> owner = userRepository.findByMoimAndMoimCategory(moim, MoimRole.OWNER);

        if (user.getIsPushAlarm() && owner.get() != user) {
            alarmService.saveAlarm(owner.get(), user, moim.getName()  + " 의 모임에 반려되셨습니다", moim.getName() + "에게 반려 되셨습니다.", AlarmType.PUSH, AlarmDetailType.MOIM, moim.getId(), null, null);
            fcmService.sendPushNotification(user, moim.getName()  + " 의 모임에 반려되셨습니다", moim.getName() + "에게 반려 되셨습니다.", AlarmDetailType.MOIM);
        }
    }

    @Override
    public void changeMoimLeader(User owner, ChangeMoimLeaderRequestDTO changeMoimLeaderRequestDTO) {
        Moim moim = moimRepository.findById(changeMoimLeaderRequestDTO.moimId()).orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));
        UserMoim userMoim = userMoimRepository.findByUserIdAndMoimId(owner.getId(), moim.getId(), JoinStatus.COMPLETE).orElseThrow(() -> new MoimException(ErrorStatus.USER_NOT_MOIM_JOIN));

        userMoim.leaveOwner();

        User target = userRepository.findById(changeMoimLeaderRequestDTO.userId()).orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));
        UserMoim targetUserMoim = userMoimRepository.findByUserIdAndMoimId(target.getId(), moim.getId(), JoinStatus.COMPLETE).orElseThrow(() -> new MoimException(ErrorStatus.USER_NOT_MOIM_JOIN));

        targetUserMoim.enterOwner();
    }

    @Override
    public void findMyRequestMoimsConfirm(User user, Long userMoimId) {
        UserMoim userMoim = userMoimRepository.findById(userMoimId).orElseThrow(() -> new MoimException(ErrorStatus.USER_NOT_MOIM_JOIN));

        userMoim.confirm();
    }

    @Override
    public void moimExpel(User user, Long userId, Long moimId) {
        Moim moim = moimRepository.findById(moimId).orElseThrow(() -> new MoimException(ErrorStatus.MOIM_NOT_FOUND));

        UserMoim ownerMoim = userMoimRepository.findByUserAndMoim(user, moim).orElseThrow(() -> new MoimException(ErrorStatus.USER_NOT_MOIM_JOIN));

        if (!ownerMoim.getMoimRole().equals(MoimRole.OWNER)) {
            throw new MoimException(ErrorStatus.USER_NOT_MOIM_ADMIN);
        }

        User target = userRepository.findById(userId).orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));

        UserMoim targetMoim = userMoimRepository.findByUserAndMoim(target, moim).orElseThrow(() -> new MoimException(ErrorStatus.USER_NOT_MOIM_JOIN));

        userMoimRepository.delete(targetMoim);
    }

    private String imageNullProcess(String imageKeyName) {
        return imageKeyName == null || imageKeyName.isEmpty() || imageKeyName.isBlank() ? null : s3Service.generateStaticUrl(imageKeyName);
    }
}
