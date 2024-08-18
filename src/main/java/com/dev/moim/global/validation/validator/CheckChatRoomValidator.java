package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.chatting.entity.ChatRoom;
import com.dev.moim.domain.chatting.repository.ChatRoomRepository;
import com.dev.moim.domain.chatting.service.ChatRoomCommandService;
import com.dev.moim.domain.chatting.service.ChatRoomQueryService;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.validation.annotation.CheckChatRoomValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CheckChatRoomValidator implements ConstraintValidator<CheckChatRoomValidation, Long> {

    private final ChatRoomQueryService chatRoomQueryService;

    @Override
    public void initialize(CheckChatRoomValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        Optional<ChatRoom> target = chatRoomQueryService.findChatRoomById(value);

        if (target.isEmpty()){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.CHATROOM_NOT_FOUND.toString())
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
