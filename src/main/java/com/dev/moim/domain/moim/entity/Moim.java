package com.dev.moim.domain.moim.entity;

import com.dev.moim.domain.moim.entity.enums.MoimCategory;
import com.dev.moim.global.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    private String imageUrl;

    @OneToMany(mappedBy = "moim")
    private List<UserMoim> userMoimList = new ArrayList<>();

    @OneToMany(mappedBy = "moim")
    private List<Plan> planList = new ArrayList<>();

    @OneToMany(mappedBy = "moim")
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "moim")
    private List<ExitReason> exitReasonList = new ArrayList<>();
}
