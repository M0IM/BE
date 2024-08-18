package com.dev.moim.domain.chatting.repository;



import com.dev.moim.domain.chatting.entity.Chat;
import com.dev.moim.domain.chatting.entity.QChat;
import com.dev.moim.domain.chatting.entity.QUserChatRoom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomChatRepositoryImpl implements CustomChatRepository {

    private final JPAQueryFactory queryFactory;
    private static final int FIRST_TAKE = 8;

    @Override
    public Slice<Chat> findChatByChatRoomIdOrderByDesc(
           Long cursor, Integer take ,Long chatRoomId
    ) {
        QChat chat = QChat.chat;

        // 기본 조건: chatRoomId로 필터링
        BooleanExpression predicate = chat.chatRoom.id.eq(chatRoomId);

        // 커서가 있는 경우, 커서 기준으로 작은 PK 값을 필터링
        if (cursor != null) {
            predicate = predicate.and(chat.id.lt(cursor));
        }

        // 쿼리 실행
        List<Chat> chats = queryFactory
                .selectFrom(chat)
                .where(predicate)
                .orderBy(chat.id.desc())
                .limit(take + 1)
                .fetch();

        // 다음 페이지가 있는지 확인
        boolean hasNext = chats.size() > take;
        if (hasNext) {
            chats.remove(take.intValue());
        }

        // 페이지 요청 생성
        PageRequest pageRequest = PageRequest.of(0, take);

        // 결과를 Slice 형태로 반환
        return new SliceImpl<>(chats, pageRequest, hasNext);
    }

    @Override
    public Slice<Chat> findChatsByLastReadTime(Long userId, Long chatRoomId) {
        QChat chat = QChat.chat;
        QUserChatRoom userChatRoom = QUserChatRoom.userChatRoom;

        // 1. 마지막 읽은 시간을 가져옵니다.
        LocalDateTime lastReadTime = queryFactory
                .select(userChatRoom.lastReadTime)
                .from(userChatRoom)
                .where(userChatRoom.user.id.eq(userId)
                        .and(userChatRoom.chatRoom.id.eq(chatRoomId)))
                .fetchOne();

        // lastReadTime 이전 꺼 하나
        List<Chat> nextChats = queryFactory
                .selectFrom(chat)
                .where(chat.chatRoom.id.eq(chatRoomId)
                        .and(chat.createdAt.lt(lastReadTime)))
                .orderBy(chat.id.desc())
                .limit(FIRST_TAKE + 1)
                .fetch();

        // lastReadTime 이후 모두 조회
        List<Chat> previousChats = queryFactory
                .selectFrom(chat)
                .where(chat.chatRoom.id.eq(chatRoomId)
                        .and(chat.createdAt.goe(lastReadTime)))
                .orderBy(chat.id.desc())
                .fetch();

        previousChats.addAll(nextChats);

        boolean hasNext = nextChats.size() > FIRST_TAKE;
        if (hasNext) {
            previousChats.remove(FIRST_TAKE);
        }

        PageRequest pageRequest = PageRequest.of(0, FIRST_TAKE);

        return new SliceImpl<>(previousChats, pageRequest, hasNext);
    }
}
