package com.dev.moim.domain.account.entity;

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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE user_profile SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction(value = "deleted_at IS NULL")
public class UserProfile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'MAIN'")
    @Column(nullable = false)
    private ProfileType profileType;

    @Column(nullable = false)
    private String name;

    private String imageUrl;

    private String introduction;

    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.REMOVE)
    private List<UserMoim> userMoimList = new ArrayList<>();

    public void addUser(User user) {
        user.getUserProfileList().add(this);
        this.user = user;
    }

    @PreRemove
    public void preRemove() {
        this.deletedAt = LocalDateTime.now();
    }

    public void updateProfileType(ProfileType profileType) {
        this.profileType = profileType;
    }

    public void updateUserProfile(String nickname, String imageUrl, String introduction) {
        this.name = nickname;
        this.imageUrl = imageUrl;
        this.introduction = introduction;
    }
}