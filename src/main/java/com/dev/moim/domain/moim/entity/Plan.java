package com.dev.moim.domain.moim.entity;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Plan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDateTime date;

    private String location;

    private String locationDetail;

    private String cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moim_id")
    private Moim moim;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<Schedule> scheduleList = new ArrayList<>();

    @OneToMany(mappedBy = "plan", cascade = CascadeType.REMOVE)
    private List<UserPlan> userPlanList = new ArrayList<>();

    public void addSchedule(Schedule schedule) {
        scheduleList.add(schedule);
        schedule.assignPlan(this);
    }

    public void updatePlan(
            String title,
            LocalDateTime date,
            String location,
            String locationDetail,
            String cost) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.locationDetail = locationDetail;
        this.cost = cost;
    }

    public void updateSchedule(List<Schedule> scheduleList) {
        this.scheduleList.clear();
        for(Schedule schedule : scheduleList) {
            this.addSchedule(schedule);
        }
    }
}
