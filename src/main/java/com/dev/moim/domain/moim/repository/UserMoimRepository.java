package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.entity.Comment;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.MoimRole;
import com.dev.moim.domain.moim.entity.enums.ProfileStatus;
import com.dev.moim.domain.moim.service.impl.dto.IntroduceVideoDTO;
import com.dev.moim.domain.moim.service.impl.dto.JoinRequestDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserMoimRepository extends JpaRepository<UserMoim, Long> {

    Boolean existsByUserIdAndMoimIdAndJoinStatus(Long userId, Long moimId, JoinStatus joinStatus);

    @Query("select um from UserMoim um where um.user.id = :userId and um.moim.id = :moimId and um.joinStatus = :joinStatus")
    Optional<UserMoim> findByUserIdAndMoimId(Long userId, Long moimId, JoinStatus joinStatus);

    @Query("select um from UserMoim um where um.moim.id = :moimId and um.joinStatus = :joinStatus")
    List<UserMoim> findByMoimId(Long moimId, JoinStatus joinStatus);

    @Query("select um from UserMoim um where um.user = :user and um.moim = :moim and um.joinStatus = 'COMPLETE'")
    Optional<UserMoim> findByUserAndMoim(User user, Moim moim);

    Boolean existsByUserAndMoim(User user, Moim moim);

    List<UserMoim> findByUserId(Long userId);

    Optional<Long> findProfileIdByUserAndMoim(User user, Moim moim);

    @Query("SELECT um " +
            "FROM UserMoim um " +
            "WHERE um.user = :user " +
            "AND um.moim = :moim " +
            "AND um.joinStatus IN :joinStatuses")
    List<UserMoim> findByUserAndMoimAndJoinRequest(User user, Moim moim, List<JoinStatus> joinStatuses);

    @Query("select new com.dev.moim.domain.moim.service.impl.dto.JoinRequestDTO(m, um) from UserMoim um join um.moim m where um.user = :user and um.confirm = false and um.id < :cursor order by um.id desc")
    Slice<JoinRequestDTO> findMyRequestMoims(User user, Long cursor, Pageable pageable);

    @Query("SELECT um FROM UserMoim um " +
            "JOIN FETCH um.moim " +
            "WHERE um.moimId = :moimId " +
            "AND um.moimRole = :moimRole ")
    Optional<UserMoim> findByMoimIdAndMoimRoleWithMoim(
            @Param("moimId") Long moimId,
            @Param("moimRole")  MoimRole moimRole);

    Optional<UserMoim> findByMoimIdAndMoimRole(Long moimId, MoimRole moimRole);

    boolean existsByUserAndMoimRole(User user, MoimRole moimRole);

    @Query("SELECT um.joinStatus FROM UserMoim um " +
            "WHERE um.user = :user AND um.moim = :moim AND um.joinStatus != 'REJECT'")
    JoinStatus findJoinStatusByUserAndMoim(User user, Moim moim);

    @Query("select um.moimRole from  UserMoim um where um.user = :user and um.moim = :moim and um.joinStatus = 'COMPLETE'")
    Optional<MoimRole> findMoimRoleByUserAndMoim(User user, Moim moim);

    @Query("select um from Comment c join c.userMoim um where c = :comment")
    Optional<UserMoim> findByComment(Comment comment);

    @Query("select um from Post p join p.userMoim um where p = :post")
    Optional<UserMoim> findByPost(Post post);

    int countByUserIdAndJoinStatus(Long userId, JoinStatus joinStatus);

    List<UserMoim> findByMoimIdAndJoinStatus(Long moimId, JoinStatus joinStatus);

    @Query("SELECT um FROM UserMoim um WHERE um.moim.id = :moimId AND um.user.id IN :userIds")
    List<UserMoim> findByMoimIdAndUserIds(@Param("moimId") Long moimId, @Param("userIds") Set<Long> userIds);

    @Query("SELECT um FROM UserMoim um " +
            "LEFT JOIN UserTodo ut ON um.user.id = ut.user.id AND ut.todo.id = :todoId " +
            "WHERE um.moim.id = :moimId AND ut.id IS NULL " +
            "AND um.joinStatus = :joinStatus " +
            "AND um.id > :cursor " +
            "ORDER BY um.id ASC")
    Slice<UserMoim> findAllMembersNotAssignedToTodo(
            @Param("moimId") Long moimId,
            @Param("todoId") Long todoId,
            @Param("joinStatus") JoinStatus joinStatus,
            @Param("cursor") Long cursor,
            Pageable pageable);

    @Query("select new com.dev.moim.domain.moim.service.impl.dto.JoinRequestDTO(m, um) from UserMoim um join um.moim m where um.user = :user and um.confirm = false and um.joinStatus = :joinStatus and um.id < :cursor order by um.id desc")
    Slice<JoinRequestDTO> findMyRequestMoimsWithJoinStatus(User user, Long cursor, JoinStatus joinStatus, PageRequest of);

    @Modifying
    @Query("delete from UserMoim um where um.confirm = true and um.joinStatus not in :joinStatusList")
    void deleteAllByConfirmUserMoim(List<JoinStatus> joinStatusList);

    @Query("SELECT um FROM UserMoim um WHERE um.user.id = :userId AND um.moim.id IN :moimIdList")
    List<UserMoim> findAllByUserIdAndMoimIdList(Long userId, List<Long> moimIdList);

    @Query("SELECT um FROM UserMoim um WHERE um.user.id = :userId AND um.moim.id IN :moimIdList AND um.joinStatus = :joinStatus")
    List<UserMoim> findAllByUserIdAndMoimIdListAndJoinStatus(Long userId, List<Long> moimIdList, JoinStatus joinStatus);

    boolean existsByUserProfileIdAndJoinStatus(Long profileId, JoinStatus joinStatus);

    @Query("SELECT um FROM UserMoim um " +
            "JOIN FETCH um.moim " +
            "WHERE um.userProfile.id = :userProfileId AND um.joinStatus = :joinStatus " +
            "AND um.id > :cursor " +
            "ORDER BY um.id ASC")
    Slice<UserMoim> findAllByUserProfileIdAndJoinStatus(
            @Param("userProfileId") Long userProfileId,
            @Param("joinStatus") JoinStatus joinStatus,
            @Param("cursor") Long cursor,
            Pageable pageable);

    @Query("SELECT um FROM UserMoim um " +
            "JOIN FETCH um.moim " +
            "JOIN FETCH um.user " +
            "WHERE um.user.id = :userId " +
            "AND um.moim.id = :moimId " +
            "AND um.joinStatus = :joinStatus")
    Optional<UserMoim> findByUserIdAndMoimIdAndJoinStatusWithUserAndMoim(Long userId, Long moimId, JoinStatus joinStatus);

    @Query("SELECT um FROM UserMoim um " +
            "JOIN FETCH um.moim " +
            "JOIN FETCH um.user " +
            "WHERE um.user.id = :userId " +
            "AND um.moim.id = :moimId " +
            "AND um.joinStatus = :joinStatus " +
            "AND um.moimRole IN :moimRoleList " )
    Optional<UserMoim> findByUserIdAndMoimIdAndJoinStatusInMoimRoleListWithUserAndMoim(Long userId, Long moimId, JoinStatus joinStatus, List<MoimRole> moimRoleList);

    @Query("SELECT um FROM UserMoim um " +
            "JOIN FETCH um.moim " +
            "JOIN FETCH um.user " +
            "WHERE um.user.id = :userId " +
            "AND um.moim.id = :moimId " +
            "AND um.joinStatus = :joinStatus " +
            "AND um.moimRole = :moimRole " )
    Optional<UserMoim> findByUserIdAndMoimIdAndJoinStatusAndMoimRoleWithUserAndMoim(Long userId, Long moimId, JoinStatus joinStatus, MoimRole moimRole);

    @Query("SELECT um FROM UserMoim um " +
            "JOIN FETCH um.user " +
            "WHERE um.moim.id = :moimId " +
            "AND um.user.id IN :userIdList ")
    List<UserMoim> findAllByMoimIdAndUserIdList(
            @Param("moimId") Long moimId,
            @Param("userIdList") List<Long> userIdList);

    @Query("SELECT um FROM UserMoim um " +
            "JOIN FETCH um.user " +
            "WHERE um.id = :userMoimId " +
            "AND um.moim.id IN :moimId ")
    Optional<UserMoim> findByIdAndMoimId(
            @Param("userMoimId") Long userMoimId,
            @Param("moimId") Long moimId);

    int countByUserIdAndJoinStatusAndProfileStatus(Long userId, JoinStatus joinStatus, ProfileStatus profileStatus);

    @Query("SELECT um.id FROM UserMoim um " +
            "WHERE um.user.id = :userId AND um.joinStatus = :joinStatus")
    List<Long> findAllUserMoimIdByUserIdAndJoinStatus(
            @Param("userId") Long userId,
            @Param("joinStatus") JoinStatus joinStatus);

    @Query("SELECT um FROM UserMoim um " +
            "WHERE um.moim.id = :moimId " +
            "AND um.joinStatus = :joinStatus " +
            "ORDER BY um.id DESC")
    List<UserMoim> findByMoimIdAndJoinStatusOrderByIdDesc(
            @Param("moimId") Long moimId,
            @Param("joinStatus") JoinStatus joinStatus,
            Pageable pageable);
}
