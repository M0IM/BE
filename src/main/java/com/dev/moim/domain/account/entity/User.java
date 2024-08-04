package com.dev.moim.domain.account.entity;

import com.dev.moim.domain.account.entity.enums.Provider;
import com.dev.moim.domain.account.entity.enums.UserRole;
import com.dev.moim.domain.account.entity.enums.UserRank;
import com.dev.moim.domain.account.entity.enums.UserStatus;
import com.dev.moim.domain.chatting.entity.UserChattingRoom;
import com.dev.moim.domain.moim.entity.ExitReason;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.UserPlan;
import com.dev.moim.global.common.BaseEntity;
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

    private String providerId;

    private LocalDateTime inactive_date;

    private String deviceId;

    private Boolean isPushAlarm;

    private Boolean isEventAlarm;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ROLE_USER'")
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'FREE'")
    private UserRank userRank;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserProfile> userProfileList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserMoim> userMoimList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserChattingRoom> userChattingRoomList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserReview> userReviewList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserPlan> userPlanList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ExitReason> exitReasonList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Alarm> alarmList = new ArrayList<>();

    public void addUserProfile(UserProfile userProfile) {
        userProfileList.add(userProfile);
        userProfile.setUser(this);
    }
}