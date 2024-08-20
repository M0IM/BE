package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.MoimRole;
import com.dev.moim.domain.moim.service.impl.dto.IntroduceVideoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserMoimRepository extends JpaRepository<UserMoim, Long> {

    Boolean existsByUserIdAndMoimId(Long userId, Long moimId);

    Optional<UserMoim> findByUserIdAndMoimId(Long userId, Long moimId);
  
    Optional<UserMoim> findByUserAndMoim(User user, Moim moim);

    Boolean existsByUserAndMoim(User user, Moim moim);

    @Query("select new com.dev.moim.domain.moim.service.impl.dto.IntroduceVideoDTO(m, up) from UserMoim um join um.userProfile up join um.moim m where um.moim.id = :moimId and um.moimRole = 'OWNER'")
    Optional<IntroduceVideoDTO> findIntroduceVideo(Long moimId);

    List<UserMoim> findByUserId(Long userId);

    Optional<Long> findProfileIdByUserAndMoim(User user, Moim moim);

    boolean existsByUserAndMoimRole(User user, MoimRole moimRole);

    @Query("SELECT COUNT(um) > 0 FROM UserMoim um " +
            "JOIN um.user u " +
            "WHERE u = :user AND um.moim = :moim AND um.joinStatus IN :joinStatuses")
    Boolean existsByUserAndMoimAndJoinStatuses(User user, Moim moim, List<JoinStatus> joinStatuses);
}
