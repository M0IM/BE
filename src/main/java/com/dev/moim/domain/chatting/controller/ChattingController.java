package com.dev.moim.domain.chatting.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "채팅 관련 컨트롤러", description = "엔드포인트 /ws")
public class ChattingController {
    
    // 채팅하기
    @GetMapping("")
    public void test() {

    }
}
