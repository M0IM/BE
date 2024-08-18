package com.dev.moim.domain.account.repository;


import com.dev.moim.domain.account.entity.User;


import java.util.List;

public interface CustomUserRepository {
    List<Long> getUserIdByChatRoomId(Long chatRoomId);

    List<User> getUserByChatRoomId(
            Long chatRoomId
    );
}
