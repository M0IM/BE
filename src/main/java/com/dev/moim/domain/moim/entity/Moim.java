package com.dev.moim.domain.moim.entity;

import com.dev.moim.domain.moim.entity.enums.MoimCategory;
import com.dev.moim.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Moim extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String location;

    private String introduction;

    @Enumerated(EnumType.STRING)
    private MoimCategory moimCategory;

    @OneToMany(mappedBy = "moim")
    private List<UserMoim> userMoimList = new ArrayList<>();

    @OneToMany(mappedBy = "moim", cascade = CascadeType.REMOVE)
    private List<MoimImage> moimImages = new ArrayList<>();
}
