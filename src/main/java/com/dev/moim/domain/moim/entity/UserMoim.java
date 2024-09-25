package com.dev.moim.domain.moim.entity;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.MoimRole;
import com.dev.moim.domain.moim.entity.enums.ProfileStatus;
import com.dev.moim.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE user_moim SET join_status = 'DELETED' WHERE id = ?")
@SQLRestriction(value = "join_status <> 'DELETED'")
public class UserMoim extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "moim_role", nullable = false)
    private MoimRole moimRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "join_status", nullable = false)
    private JoinStatus joinStatus;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean confirm;

    @Enumerated(EnumType.STRING)
    @Column(name = "profile_status", nullable = false)
    private ProfileStatus profileStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moim_id")
    private Moim moim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;

    public void accept() {
        this.joinStatus = JoinStatus.COMPLETE;
    }

    public void reject() {
        this.joinStatus = JoinStatus.REJECT;
    }

    public void changeStatus(MoimRole moimRole) {
        this.moimRole = moimRole;
    }

    public void updateProfileStatus (ProfileStatus profileStatus) {
        this.profileStatus = profileStatus;
    }

    public void leaveOwner () {
        this.moimRole = MoimRole.ADMIN;
    }

    public void enterOwner () {
        this.moimRole = MoimRole.OWNER;
    }

    public void confirm () {
        this.confirm = true;
    }

    public void updateUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
