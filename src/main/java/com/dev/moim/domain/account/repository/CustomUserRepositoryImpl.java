package com.dev.moim.domain.account.repository;


import com.dev.moim.domain.account.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.dev.moim.domain.account.entity.QUser.user;
import static com.dev.moim.domain.chatting.entity.QChatRoom.chatRoom;
import static com.dev.moim.domain.chatting.entity.QUserChatRoom.userChatRoom;


@Repository
@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> getUserIdByChatRoomId(
            Long chatRoomId
    ) {
        return queryFactory
                .select(user.id)
                .from(userChatRoom)
                .join(userChatRoom.user, user)
                .join(userChatRoom.chatRoom, chatRoom)
                .where(chatRoom.id.eq(chatRoomId))
                .fetch();
    }

    @Override
    public List<User> getUserByChatRoomId(
            Long chatRoomId
    ) {
        return queryFactory
                .select(user)
                .from(userChatRoom)
                .join(userChatRoom.user, user)
                .join(userChatRoom.chatRoom, chatRoom)
                .where(chatRoom.id.eq(chatRoomId))
                .fetch();
    }
}
