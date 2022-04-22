package com.back.snobs.service.redispubsub;

import com.back.snobs.dto.chatroom.chatmessage.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessagingTemplate template;

    // 메시지 발행 시 대기하고 있던 onMessage 실행
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            Object obj = null;
            ByteArrayInputStream bis = new ByteArrayInputStream(message.getBody());
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();

            ChatMessage chatMessage = (ChatMessage) obj;
            template.convertAndSend("/ws/sub/chat/room/" + chatMessage.getChatRoomIdx(), chatMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
