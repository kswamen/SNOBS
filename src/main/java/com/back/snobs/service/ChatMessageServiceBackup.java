package com.back.snobs.service;

import com.back.snobs.domain.chatroom.chatmessage.ChatMessage;
import com.back.snobs.domain.chatroom.chatmessage.ChatMessageDto;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.error.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceBackup implements ChatMessageServiceInterface{
    private final RedisTemplate redisTemplate;
    private ZSetOperations<String, ChatMessage> zSetOperations;

    @PostConstruct
    private void init() {
        zSetOperations = redisTemplate.opsForZSet();
    }

    public ChatMessageDto saveMessage(ChatMessageDto chatMessageDto) {
        long now = System.currentTimeMillis();
        chatMessageDto.setCreateDate(now);
        ChatMessage chatMessage = chatMessageDto.toEntity();
        zSetOperations.add(getChatRoomKey(chatMessage.getChatRoomIdx()), chatMessage, now);
        return chatMessageDto;
    }

    public ResponseEntity<CustomResponse> getMessage(Long chatRoomIdx) {
        Set<ChatMessage> messages = zSetOperations.range(getChatRoomKey(chatRoomIdx), -1000, -1);
        assert messages != null;
        List<ChatMessage> result = new ArrayList<>(messages);
        return new ResponseEntity<>(new CustomResponse(ResponseCode.SUCCESS, result), HttpStatus.valueOf(200));
    }

    public String getChatRoomKey(Long chatRoomIdx) {
        return "CHATROOM" + chatRoomIdx;
    }
}
