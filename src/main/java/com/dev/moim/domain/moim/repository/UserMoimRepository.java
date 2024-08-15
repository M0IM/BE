package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.UserMoim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserMoimRepository extends JpaRepository<UserMoim, Long> {

    Boolean existsByUserIdAndMoimId(Long userId, Long moimId);

    Optional<UserMoim> findByUserIdAndMoimId(Long userId, Long moimId);
  
    Optional<UserMoim> findByUserAndMoim(User user, Moim moim);

    Boolean existsByUserAndMoim(User user, Moim moim);

    List<UserMoim> findByUserId(Long userId);
}
