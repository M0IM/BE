package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.MoimRole;
import com.dev.moim.domain.moim.service.impl.dto.IntroduceVideoDTO;
import com.dev.moim.domain.moim.service.impl.dto.JoinRequestDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserMoimRepository extends JpaRepository<UserMoim, Long> {

    Boolean existsByUserIdAndMoimIdAndJoinStatus(Long userId, Long moimId, JoinStatus joinStatus);

    @Query("select um from UserMoim um where um.user.id = :userId and um.moim.id = :moimId and um.joinStatus = :joinStatus")
    Optional<UserMoim> findByUserIdAndMoimId(Long userId, Long moimId, JoinStatus joinStatus);
  
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

    @Query("select um.moimRole from  UserMoim um where um.user = :user and um.joinStatus = 'COMPLETE'")
    Optional<MoimRole> findMoimRoleByUser(User user);
}
