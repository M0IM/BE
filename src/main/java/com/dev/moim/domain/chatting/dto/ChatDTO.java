package com.dev.moim.domain.chatting.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class ChatDTO {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ChatListResponse {
        private List<ChatResponse> chatResponseList;
        private Long nextCursor;
        private Boolean hasNext;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ChatResponse {
        private Long chatId;
        private String content;
        private String imageKeyName;
        private LocalDateTime createAt;
        private SenderDTO senderDTO;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SenderDTO {
        private Long senderId;
        private String senderName;
        private String senderProfile;
    }

}
