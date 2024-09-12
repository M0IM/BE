package com.dev.moim.domain.account.repository;

import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.enums.ProfileType;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.service.impl.dto.UserProfileDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUserIdAndProfileType(Long userId, ProfileType profileType);

    @Query("select new com.dev.moim.domain.moim.service.impl.dto.UserProfileDTO(up, um) from UserMoim um join um.userProfile up where um.moim = :moim and um.joinStatus = :joinStatus")
    List<UserProfileDTO> findRandomProfile(Moim moim, JoinStatus joinStatus, Pageable pageable);

    @Query("SELECT up FROM UserProfile up " +
            "WHERE up.user.id = :userId " +
            "AND up.id > :cursor " +
            "ORDER BY up.id ASC")
    Slice<UserProfile> findAllByUserIdAndCursor(
            @Param("userId") Long userId,
            @Param("cursor") Long cursor,
            Pageable pageable);
}
