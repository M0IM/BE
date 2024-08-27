package com.dev.moim.domain.account.entity;

import com.dev.moim.domain.account.entity.enums.*;
import com.dev.moim.domain.moim.entity.IndividualPlan;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.UserPlan;
import com.dev.moim.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@SQLDelete(sql = "UPDATE user SET status = 'OFF', inactive_date = current_timestamp WHERE id = ?")
@SQLRestriction(value = "status = 'ON'")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    private String providerId;

    private LocalDateTime inactive_date;

    private String deviceId;

    @Column(nullable = false)
    @ColumnDefault("true")
    private Boolean isPushAlarm;

    @Column(nullable = false)
    @ColumnDefault("true")
    private Boolean isEventAlarm;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'FEMALE'")
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate birth;

    private double rating;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ROLE_USER'")
    @Column(nullable = false)
    private UserRole userRole;

    @Column(nullable = false)
    @ColumnDefault("'ON'")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'FREE'")
    @Column(nullable = false)
    private UserRank userRank;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserProfile> userProfileList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<UserMoim> userMoimList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<UserPlan> userPlanList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<IndividualPlan> individualPlanList = new ArrayList<>();

    public void updateRating(double newRating) {
        this.rating = newRating;
    }

    public void changePushAlarm() {
        this.isPushAlarm = !this.isPushAlarm;
    }

    public void changeEventAlarm() {
        this.isEventAlarm = !this.isEventAlarm;
    }

    public void updatePassword(String newPassword) {this.password = newPassword;}

    public void updateDeviceId(String fcmToken) {
        this.deviceId = fcmToken;
    }

    public void fcmSignOut() {
        this.deviceId = null;
    }
}