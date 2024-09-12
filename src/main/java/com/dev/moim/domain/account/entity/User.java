package com.dev.moim.domain.account.entity;

import com.dev.moim.domain.account.entity.enums.*;
import com.dev.moim.domain.moim.entity.*;
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

    @Column(nullable = false)
    private LocalDateTime lastAlarmTime;

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

    @Column(nullable = false)
    private String residence;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<UserTodo> userTodoList = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Todo> todoList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ReadPost> readPostList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Alarm> alarmList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<PostReport> postReportList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<PostBlock> postBlockList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<PostLike> postLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<CommentBlock> commentBlockList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<CommentLike> commentLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<CommentReport> commentReportList = new ArrayList<>();

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

    public void updateAlarmTime() {
        this.lastAlarmTime = LocalDateTime.now();
    }

    public void updateUserInfo(String residence, Gender gender, LocalDate birth) {
        this.residence = residence;
        this.gender = gender;
        this.birth = birth;
    }

    @PreRemove
    public void preRemove() {
        for (Todo todo : todoList) {
            todo.updateWriter(null);
        }
    }
}