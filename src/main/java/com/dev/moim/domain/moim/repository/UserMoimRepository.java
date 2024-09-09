package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.entity.Comment;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.MoimRole;
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

    @Query("select new com.dev.moim.domain.moim.service.impl.dto.IntroduceVideoDTO(m, up) from UserMoim um join um.userProfile up join um.moim m where um.moim.id = :moimId and um.moimRole = 'OWNER'")
    Optional<IntroduceVideoDTO> findIntroduceVideo(Long moimId);

    List<UserMoim> findByUserId(Long userId);

    Optional<Long> findProfileIdByUserAndMoim(User user, Moim moim);

    @Query("SELECT COUNT(um) > 0 " +
            "FROM UserMoim um " +
            "WHERE um.user = :user " +
            "AND um.moim = :moim " +
            "AND um.joinStatus IN :joinStatuses")
    Boolean findByUserAndMoimAndJoinRequest(User user, Moim moim, List<JoinStatus> joinStatuses);

    @Query("select new com.dev.moim.domain.moim.service.impl.dto.JoinRequestDTO(m, um) from UserMoim um join um.moim m where um.user = :user and um.confirm = false and um.id < :cursor order by um.id desc")
    Slice<JoinRequestDTO> findMyRequestMoims(User user, Long cursor, Pageable pageable);

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
}
