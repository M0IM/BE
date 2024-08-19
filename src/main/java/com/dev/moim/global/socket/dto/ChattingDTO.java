package com.dev.moim.global.socket.dto;

import lombok.Getter;
import lombok.Setter;

public class ChattingDTO {

    @Getter
    @Setter
    public static class MessageDTO {
        private ChatType chatType;
        private String content;
        private Long chatRoomId;
        private String imageKeyName;

        public enum ChatType {
            ENTER, TALK, EXIT
        }
    }
}
