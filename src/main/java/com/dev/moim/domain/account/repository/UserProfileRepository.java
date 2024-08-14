package com.dev.moim.domain.account.repository;

import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.enums.ProfileType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUserIdAndProfileType(Long userId, ProfileType profileType);
}
