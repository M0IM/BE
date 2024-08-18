package com.dev.moim.domain.chatting.service;


import com.dev.moim.domain.account.entity.enums.Provider;
import com.dev.moim.domain.chatting.entity.Chat;
import com.dev.moim.global.socket.dto.ChattingDTO;
import org.springframework.transaction.annotation.Transactional;

public interface ChatCommandService {


    // DB에 넣는 API
    @Transactional
    Chat saveChat(ChattingDTO.MessageDTO messageDTO, Long userId);
}
