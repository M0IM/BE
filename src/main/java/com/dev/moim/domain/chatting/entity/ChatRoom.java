package com.dev.moim.domain.chatting.entity;


import com.dev.moim.domain.chatting.dto.ChatRoomDTO;
import com.dev.moim.domain.chatting.dto.ChatRoomDTO.UpdateChatRoomRequest;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String imageKeyName;

    public void updateChatRoom(UpdateChatRoomRequest updateChatRoomRequest) {
        this.title = updateChatRoomRequest.getTitle();
        this.imageKeyName = updateChatRoomRequest.getImageKeyName();
    }
}
