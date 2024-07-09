package com.dev.moim.domain.account.repository;

import com.dev.moim.domain.account.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByEmail(String email);

    void deleteRefreshTokenByToken(String refresh);
}
