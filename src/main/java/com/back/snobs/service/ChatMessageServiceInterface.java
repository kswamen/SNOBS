package com.back.snobs.service;

import com.back.snobs.dto.chatroom.chatmessage.ChatMessageDto;
import com.back.snobs.error.CustomResponse;
import org.springframework.http.ResponseEntity;

public interface ChatMessageServiceInterface {
    public ChatMessageDto saveMessage(ChatMessageDto chatMessageDto);
    public ResponseEntity<CustomResponse> getMessage(Long chatRoomIdx);
}
