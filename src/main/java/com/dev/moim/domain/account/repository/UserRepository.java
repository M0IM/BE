package com.dev.moim.domain.account.repository;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.enums.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByProviderIdAndProvider(String providerId, Provider provider);
}