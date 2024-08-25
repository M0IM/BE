package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.enums.MoimCategory;
import com.dev.moim.domain.moim.service.impl.dto.JoinRequestDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MoimRepository extends JpaRepository<Moim, Long> {

    @Query("SELECT m FROM UserMoim um join um.moim m where um.user = :user and m.id < :cursor order by m.id desc")
    Slice<Moim> findMyMoims(User user, Long cursor, Pageable pageable);

    Slice<Moim> findByMoimCategoryAndNameLikeAndIdLessThanOrderByIdDesc(MoimCategory moimCategory, String name, Long id, Pageable pageable);

    Slice<Moim> findByNameLikeAndIdLessThanOrderByIdDesc(String name, Long id, Pageable pageable);

    Slice<Moim> findByIdLessThanOrderByIdDesc(Long Id, Pageable pageable);

    @Query("select m from UserMoim um join um.moim m where um.user = :user")
    List<Moim> findMoimsByUser(User user);
}
