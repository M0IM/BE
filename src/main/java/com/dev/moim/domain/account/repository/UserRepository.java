package com.dev.moim.domain.account.repository;

import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.MoimCategory;
import com.dev.moim.domain.moim.entity.enums.MoimRole;
import com.dev.moim.domain.moim.service.impl.dto.UserProfileDTO;
import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.enums.Provider;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
    Optional<User> findByEmailAndProvider(String email, Provider provider);

    Optional<User> findByProviderIdAndProvider(String providerId, Provider provider);

    boolean existsByEmail(String email);

    boolean existsByProviderAndProviderId(Provider provider, String providerId);

    @Query("select new com.dev.moim.domain.moim.service.impl.dto.UserProfileDTO(up, um) " +
            "from UserMoim um " +
            "join um.userProfile up " +
            "where um.moim.id = :moimId and um.joinStatus = :joinStatus and um.id > :cursor")
    Slice<UserProfileDTO> findUserByMoimId(Long moimId, JoinStatus joinStatus, Long cursor,  Pageable pageable);

    @Query("select u from UserMoim um join um.user u where um.moim = :moim and um.joinStatus = :joinStatus")
    List<User> findUserByMoim(Moim moim, JoinStatus joinStatus);

    @Query("select u from UserMoim um join um.user u where um.moim = :moim and um.moimRole = :moimRole")
    Optional<User> findByMoimAndMoimCategory(Moim moim, MoimRole moimRole);
}