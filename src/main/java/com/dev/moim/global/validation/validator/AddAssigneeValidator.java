package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.moim.dto.todo.AddTodoAssigneeDTO;
import com.dev.moim.domain.moim.service.MoimQueryService;
import com.dev.moim.domain.moim.service.TodoQueryService;
import com.dev.moim.global.validation.annotation.AddAssigneeValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddAssigneeValidator implements ConstraintValidator<AddAssigneeValidation, AddTodoAssigneeDTO> {

    private final TodoQueryService todoQueryService;
    private final MoimQueryService moimQueryService;

    @Override
    public void initialize(AddAssigneeValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(AddTodoAssigneeDTO request, ConstraintValidatorContext context) {

        List<Long> addAssigneeIdList = request.addAssigneeIdList();
        List<Long> moimMemberIdList = moimQueryService.findAllMemberIdByMoimId(request.moimId());

        boolean hasNonMember = addAssigneeIdList.stream()
                .anyMatch(id -> !moimMemberIdList.contains(id));
        if (hasNonMember) {
            addConstraintViolation(context, INVALID_MOIM_MEMBER.getMessage());
            return false;
        }

        List<Long> assigneeIdList = todoQueryService.findAssigneeByTodoId(request.todoId()).stream()
                .map(userTodo -> userTodo.getUser().getId())
                .toList();

        boolean hasDuplicate = addAssigneeIdList.stream()
                .anyMatch(assigneeIdList::contains);
        if (hasDuplicate) {
            addConstraintViolation(context, IS_ALREADY_TODO_ASSIGNEE.getMessage());
            return false;
        }
        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode("addAssigneeIdList")
                .addConstraintViolation();
    }
}