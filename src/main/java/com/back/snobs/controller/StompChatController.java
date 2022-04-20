package com.back.snobs.controller;

import com.back.snobs.dto.chatroom.chatmessage.ChatMessageDto;
import com.back.snobs.service.ChatMessageService;
import com.back.snobs.service.ChatMessageServiceRdb;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StompChatController {
    private final ChatMessageService chatMessageService;
//    private final ChatMessageServiceRdb chatMessageService;
    private final SimpMessagingTemplate template;

    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessageDto chatMessageDto) {
        chatMessageDto.setMessage("새 유저가 채팅방에 참여하였습니다.");
        template.convertAndSend("/ws/sub/chat/room/" + chatMessageDto.getChatRoomIdx(), chatMessageDto);
    }

    // (prefix)/ws/pub + /chat/message
    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDto chatMessageDto) {
        chatMessageDto = chatMessageService.saveMessage(chatMessageDto);
        // 해당 경로를 구독한 구독자들에게 전파
        template.convertAndSend("/ws/sub/chat/room/" + chatMessageDto.getChatRoomIdx(), chatMessageDto);
    }
}
