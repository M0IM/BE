package com.dev.moim.domain.account.entity;

import com.dev.moim.domain.account.entity.enums.Provider;
import com.dev.moim.domain.account.entity.enums.UserRank;
import com.dev.moim.domain.account.entity.enums.UserStatus;
import com.dev.moim.domain.chatting.entity.UserChattingRoom;
import com.dev.moim.domain.moim.entity.UserMoim;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    private UserRank userRank;

    @OneToMany(mappedBy = "user")
    private List<UserProfile> userProfileList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<SNS> snsList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserMoim> userMoimList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserChattingRoom> userChattingRoomList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Review> reviewList = new ArrayList<>();
}
