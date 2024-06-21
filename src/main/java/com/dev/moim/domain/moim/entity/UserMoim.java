package com.dev.moim.domain.moim.entity;

import com.dev.moim.domain.account.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserMoim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moim_id")
    private Moim moim;

    @OneToMany(mappedBy = "userMoim")
    private List<Calendar> calendarList = new ArrayList<>();

    @OneToMany(mappedBy = "userMoim")
    private List<GroupAlarm> groupAlarmList = new ArrayList<>();

    @OneToMany(mappedBy = "userMoim")
    private List<ExitReason> exitReasonList = new ArrayList<>();

    @OneToMany(mappedBy = "userMoim")
    private List<TakingOverPost> takingOverPostList = new ArrayList<>();

    @OneToMany(mappedBy = "userMoim")
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "userMoim")
    private List<Comment> commentList = new ArrayList<>();
}
