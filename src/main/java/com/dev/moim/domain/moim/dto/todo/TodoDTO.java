package com.dev.moim.domain.moim.dto.todo;

import com.dev.moim.domain.moim.entity.Todo;
import com.dev.moim.domain.moim.entity.TodoImage;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.MoimRole;
import com.dev.moim.domain.moim.entity.enums.TodoStatus;

import java.time.LocalDateTime;
import java.util.List;

public record TodoDTO(
        Long todoId,
        String title,
        LocalDateTime dueDate,
        List<String> imageUrlList,
        TodoStatus todoStatus,
        String writerNickname,
        String writerProfileImageUrl,
        MoimRole writerMoimRole,
        Long moimId,
        String moimName
) {

    public static TodoDTO forMoimAdmins(Todo todo, UserMoim userMoim) {
        return new TodoDTO(
                todo.getId(),
                todo.getTitle(),
                todo.getDueDate(),
                todo.getTodoImageList().stream().map(TodoImage::getImageUrl).toList(),
                todo.getStatus(),
                userMoim.getUserProfile().getName(),
                userMoim.getUserProfile().getImageUrl(),
                userMoim.getMoimRole(),
                todo.getMoim().getId(),
                todo.getMoim().getName()
        );
    }

    public static TodoDTO forSpecificAdmin(Todo todo) {
        return new TodoDTO(
                todo.getId(),
                todo.getTitle(),
                todo.getDueDate(),
                todo.getTodoImageList().stream().map(TodoImage::getImageUrl).toList(),
                todo.getStatus(),
                null,
                null,
                null,
                todo.getMoim().getId(),
                todo.getMoim().getName()
        );
    }
}
