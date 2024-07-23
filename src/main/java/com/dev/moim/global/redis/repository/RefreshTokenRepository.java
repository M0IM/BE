package com.dev.moim.global.redis.repository;

import com.dev.moim.global.redis.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByEmail(String email);

    void deleteRefreshTokenByEmail(String email);
}
