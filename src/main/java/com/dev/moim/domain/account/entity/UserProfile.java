package com.dev.moim.domain.account.entity;

import com.dev.moim.domain.account.entity.enums.Gender;
import com.dev.moim.domain.account.entity.enums.ProfileType;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserProfile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'SUB'")
    private ProfileType profileType;

    private String name;

    private String imageUrl;

    private String residence;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'FEMALE'")
    private Gender gender;

    private String birth;

    private String introduction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL)
    private List<UserMoim> userMoimList = new ArrayList<>();

    public void setUser(User user) {
        this.user = user;
    }
}
