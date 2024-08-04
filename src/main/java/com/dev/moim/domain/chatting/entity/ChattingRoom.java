package com.dev.moim.domain.chatting.entity;

import com.dev.moim.global.common.BaseEntity;
import jakarta.persistence.*;
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
@Table(name = "chatting_room")
public class ChattingRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatting_room_id")
    private Long id;

    private String title;

    @OneToMany(mappedBy = "chattingRoom")
    private List<UserChattingRoom> userChattingRoomList = new ArrayList<>();

    @OneToMany(mappedBy = "chattingRoom")
    private List<Chatting> chattingList = new ArrayList<>();
}
