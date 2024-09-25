package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.enums.MoimCategory;
import com.dev.moim.domain.moim.entity.enums.MoimRole;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MoimRepository extends JpaRepository<Moim, Long> {

    @Query("SELECT m FROM UserMoim um join um.moim m where um.user.id = :userId and um.joinStatus = 'COMPLETE' and m.id < :cursor order by m.id desc")
    Slice<Moim> findMyMoims(Long userId, Long cursor, Pageable pageable);

    @Query("SELECT m FROM Moim m WHERE m.id < :id AND m.moimCategory IN :moimCategories AND m.name LIKE %:name% order by m.id desc")
    Slice<Moim> findByMoimCategoryAndNameLikeAndIdLessThanOrderByIdDesc(
            List<MoimCategory> moimCategories,
            String name,
            Long id,
            Pageable pageable
    );


    Slice<Moim> findByNameLikeAndIdLessThanOrderByIdDesc(String name, Long id, Pageable pageable);

    Slice<Moim> findByIdLessThanOrderByIdDesc(Long Id, Pageable pageable);

    @Query("select m from UserMoim um join um.moim m where um.user = :user and um.joinStatus = 'COMPLETE'")
    List<Moim> findMoimsByUser(User user);

    @Query("SELECT m FROM UserMoim um join um.moim m where um.user.id = :userId and um.joinStatus = 'COMPLETE' and um.moimRole = :moimRole and m.id < :cursor order by m.id desc")
    Slice<Moim> findMyMoimsWithMoimRole(Long userId, Long cursor, MoimRole moimRole, PageRequest of);
}
