package com.dev.moim.domain.account.entity;

import com.dev.moim.domain.account.entity.enums.*;
import com.dev.moim.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

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
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String providerId;

    private LocalDateTime inactive_date;

    private String deviceId;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isPushAlarm;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isEventAlarm;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'FEMALE'")
    private Gender gender;

    private LocalDate birth;

    private double rating;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ROLE_USER'")
    private UserRole userRole;

    @Column(nullable = false)
    @ColumnDefault("'ON'")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'FREE'")
    private UserRank userRank;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserProfile> userProfileList = new ArrayList<>();

    public void updateRating(double newRating) {
        this.rating = newRating;
    }

    public void changePushAlarm() {
        this.isPushAlarm = !this.isPushAlarm;
    }

    public void changeEventAlarm() {
        this.isEventAlarm = !this.isEventAlarm;
    }

    public void fcmSignOut() {
        this.deviceId = null;
    }
}