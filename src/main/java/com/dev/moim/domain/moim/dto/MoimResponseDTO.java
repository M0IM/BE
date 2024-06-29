package com.dev.moim.domain.moim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MoimResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateMoimResultDTO {
        private Long moimId;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateMoimPostResultDTO {
        private Long moimPostId;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MoimPreviewDTO {
        private Long moimId;
        private String title;
        private String description;
        private String category;
        private String address;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MoimPreviewListDTO {
        private List<MoimPreviewDTO> moimPreviewList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MoimPostPreviewListDTO {
        private List<MoimPostPreviewDTO> moimPreviewList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MoimDetailDTO {
        private Long moimId;
        private String title;
        private String description;
        private List<String> category;
        private String address;
        private Boolean status;
        private List<MoimCalendarDTO> moimCalendarDTOList;
        private List<MoimAnnouncementPreviewDTO> moimPostPreviewDTOList;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MoimCalendarDTO {
        private Long moimCalendarId;
        private String title;
        private String cost;
        private String participant;
        private String address;
        private LocalDate date;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MoimPostPreviewDTO {
        private Long moimPostId;
        private String title;
        private String content;
        private String writer;
        private Integer commentCount;
        private Integer likeCount;
        private LocalDateTime createAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MoimPostDetailDTO {
        private Long moimPostId;
        private String title;
        private String content;
        private String writer;
        private Integer commentCount;
        private Integer likeCount;
        private List<MoimCommentDTO> moimCommentDTOList;
        private LocalDateTime updateAt;
        private LocalDateTime createAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MoimCommentDTO {
        private Long moimCommentId;
        private String title;
        private String content;
        private String writer;
        private Integer likeCount;
        private LocalDateTime updateAt;
        private LocalDateTime createAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MoimAnnouncementPreviewDTO {
        private Long announcementId;
        private String title;
        private String content;
        private String writer;
    }

}
