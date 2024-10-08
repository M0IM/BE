package com.dev.moim.domain.account.repository;

import com.dev.moim.domain.account.entity.Alarm;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.MoimRole;
import com.dev.moim.domain.moim.service.impl.dto.UserProfileDTO;
import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.enums.Provider;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndProvider(String email, Provider provider);

    Optional<User> findByProviderIdAndProvider(String providerId, Provider provider);

    boolean existsByEmail(String email);

    boolean existsByProviderAndProviderId(Provider provider, String providerId);

    @Query("select new com.dev.moim.domain.moim.service.impl.dto.UserProfileDTO(up, um) " +
            "from UserMoim um " +
            "join um.userProfile up " +
            "where um.moim.id = :moimId " +
            "and um.joinStatus = :joinStatus " +
            "and um.id > :cursor " +
            "and up.name like %:searching% " +
            "order by um.id")
    Slice<UserProfileDTO> findUserByMoimId(Long moimId, String searching, JoinStatus joinStatus, Long cursor,  Pageable pageable);

    @Query("select u from UserMoim um join um.user u where um.moim = :moim and um.joinStatus = :joinStatus")
    List<User> findUserByMoim(Moim moim, JoinStatus joinStatus);

    @Query("select u from UserMoim um join um.user u where um.moim = :moim and um.moimRole = :moimRole")
    Optional<User> findByMoimAndMoimCategory(Moim moim, MoimRole moimRole);

    @Query("select a from Alarm a join a.user u where u.lastAlarmTime < a.createdAt and u = :user")
    List<Alarm> findAlarmByUser(User user);

    @Modifying
    @Query("update User u set u.deviceId = null where u = :user")
    void updateFcmTokenByUser(User user);
    @Query("select rp.user.id from ReadPost rp where rp.post = :post and rp.isRead = false")
    Set<Long> findReadUserId(Post post);

    @Query("select new com.dev.moim.domain.moim.service.impl.dto.UserProfileDTO(up, um) from UserMoim um join um.userProfile up where um.user.id in :userIds and um.moim = :moim and um.joinStatus = 'COMPLETE'")
    List<UserProfileDTO> findReadUsersProfileByUsersId(Set<Long> userIds, Moim moim);

    @Query("select um from UserMoim um where um.moim = :moim and um.moimRole = 'OWNER' and um.joinStatus = 'COMPLETE'")
    Optional<UserMoim> findOwnerByMoim(Moim moim);

    @Query("select um.user from UserMoim um where um.moim = :moim and (um.moimRole = 'ADMIN' or um.moimRole = 'OWNER')")
    List<User> findAdmins(Moim moim);

    @Modifying
    @Query("update User u set u.lastAlarmTime = :lastReadTime where u = :user")
    void updateLastReadTime(User user, LocalDateTime lastReadTime);

    List<User> findAllByIsPushAlarmTrueAndDeviceIdNotNull();

    @Query("select new com.dev.moim.domain.moim.service.impl.dto.UserProfileDTO(up, um) " +
            "from UserMoim um " +
            "join um.userProfile up " +
            "where um.moim.id = :moimId " +
            "and um.joinStatus = :joinStatus " +
            "and um.moimRole <> 'OWNER' " +
            "and um.id > :cursor " +
            "and up.name like %:searching% " +
            "order by um.id")
    Slice<UserProfileDTO> findUserByMoimIdExcludeOwner(Long moimId, String searching, JoinStatus joinStatus, Long cursor, Pageable pageable);
}