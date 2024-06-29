package com.dev.moim.domain.moim.dto;

import lombok.Getter;

import java.util.List;

public class MoimRequestDTO {

    @Getter
    public static class CreateMoimDTO {
        private String title;
        private String address;
        private String category;
        private String description;
        private List<String> imageUrl;
        private String tag;
    }

    @Getter
    public static class WithMoimDTO {
        private Long moimId;
        private String exitReason;
    }

    @Getter
    public static class UpdateMoimDTO {
        private Long moimId;
        private String title;
        private String address;
        private String category;
        private String description;
        private List<String> imageUrl;
        private String tag;
    }

    @Getter
    public static class CreateMoimPostDTO {
        private String title;
        private String content;
        private List<String> imageUrl;
    }
}
