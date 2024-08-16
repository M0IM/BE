package com.dev.moim.domain.account.entity;

import com.dev.moim.domain.account.entity.enums.Gender;
import com.dev.moim.domain.account.entity.enums.ProfileType;
import com.dev.moim.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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
    @ColumnDefault("'MAIN'")
    private ProfileType profileType;

    private String name;

    private String imageFileName;

    private String residence;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'FEMALE'")
    private Gender gender;

    private String birth;

    private String introduction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void addUser(User user) {
        user.getUserProfileList().add(this);
        this.user = user;
    }

    public void updateUser(String name, String residence, String introduction) {
        this.name = name;
        this.residence = residence;
        this.introduction = introduction;
    }
}