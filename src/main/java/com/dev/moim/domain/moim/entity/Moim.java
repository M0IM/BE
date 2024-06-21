package com.dev.moim.domain.moim.entity;

import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
    private UserMoim.MoimType moimType;

    @Column(name = "moim_image")
    private String moimImage;

    @OneToMany(mappedBy = "moim")
    private List<UserMoim> userMoimList = new ArrayList<>();

    @OneToMany(mappedBy = "moim")
    private List<MoimAccount> moimAccountList = new ArrayList<>();

    @OneToMany(mappedBy = "moim")
    private List<MoimTag> moimTagList = new ArrayList<>();

    @OneToMany(mappedBy = "moim")
    private List<MoimCategory> moimCategoryList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dashboard_id")
    private Dashboard dashboard;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;
}
