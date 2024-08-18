package com.dev.moim.domain.account.repository;

import com.dev.moim.domain.account.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
