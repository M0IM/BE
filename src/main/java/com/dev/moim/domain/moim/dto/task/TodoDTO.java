package com.dev.moim.domain.moim.dto.task;

import com.dev.moim.domain.moim.entity.Todo;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.MoimRole;

import java.time.LocalDateTime;

public record TodoDTO(
        Long todoId,
        String title,
        LocalDateTime dueDate,
        String writerNickname,
        String writerProfileImageUrl,
        MoimRole writerMoimRole
) {

    public static TodoDTO forAdmin(Todo todo, UserMoim userMoim) {
        return new TodoDTO(
                todo.getId(),
                todo.getTitle(),
                todo.getDueDate(),
                userMoim.getUserProfile().getName(),
                userMoim.getUserProfile().getImageUrl(),
                userMoim.getMoimRole()
        );
    }

    public static TodoDTO forSpecificAdmin(Todo todo) {
        return new TodoDTO(
                todo.getId(),
                todo.getTitle(),
                todo.getDueDate(),
                null,
                null,
                null
        );
    }
}
