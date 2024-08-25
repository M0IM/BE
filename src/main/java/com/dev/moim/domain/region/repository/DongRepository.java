package com.dev.moim.domain.region.repository;

import com.dev.moim.domain.region.entity.Dong;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DongRepository extends JpaRepository<Dong, Long> {
    List<Dong> findByParentId(Long parentId);

    @Query("SELECT d FROM Dong d " +
            "JOIN d.parent sg " +
            "JOIN sg.parent s " +
            "WHERE (s.addrName LIKE %:searchTerm% " +
            "OR sg.addrName LIKE %:searchTerm% " +
            "OR d.addrName LIKE %:searchTerm%) " +
            "AND (d.id >= :cursor) " +
            "ORDER BY d.id ASC")
    Slice<Dong> searchAddress(@Param("searchTerm") String searchTerm, @Param("cursor") Long cursor, Pageable pageable);
}
