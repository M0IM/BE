package com.dev.moim.domain.account.repository;

import com.dev.moim.domain.account.entity.Alarm;
import com.dev.moim.domain.account.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    @Modifying
    @Query("delete from Alarm a where a.user = :user")
    void deleteAllByUser(User user);
}
