package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.entity.Moim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MoimRepository extends JpaRepository<Moim, Long> {

    @Query("SELECT m FROM UserMoim um join um.moim m where um.user = :user")
    List<Moim> findMyMoims(User user);
}
