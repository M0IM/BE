package com.dev.moim.domain.account.entity;

import com.dev.moim.domain.account.entity.enums.Provider;
import com.dev.moim.domain.account.entity.enums.Role;
import com.dev.moim.domain.account.entity.enums.UserRank;
import com.dev.moim.domain.account.entity.enums.UserStatus;
import com.dev.moim.domain.chatting.entity.UserChattingRoom;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.global.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.dev.moim.domain.account.entity.enums.Role.ROLE_USER;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private LocalDateTime inactive_date;

    private String deviceId;

    private Long writerId;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ROLE_USER'")
    private Role role;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    private UserRank userRank;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserProfile> userProfileList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<SNS> snsList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserMoim> userMoimList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserChattingRoom> userChattingRoomList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Review> reviewList = new ArrayList<>();

    public void addUserProfile(UserProfile userProfile) {
        userProfileList.add(userProfile);
        userProfile.setUser(this);
    }
}
