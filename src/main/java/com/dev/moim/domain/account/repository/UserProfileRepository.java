package com.dev.moim.domain.account.repository;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.enums.ProfileType;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUserIdAndProfileType(Long userId, ProfileType profileType);

    @Query("select up from UserMoim um join um.userProfile up where um.moim = :moim and um.joinStatus = :joinStatus and up.imageUrl is not null")
    List<UserProfile> findRandomProfile(Moim moim, JoinStatus joinStatus, Pageable pageable);
}
