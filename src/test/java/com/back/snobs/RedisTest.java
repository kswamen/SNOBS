package com.back.snobs;

import com.back.snobs.dto.chatroom.ChatRoom;
import com.back.snobs.dto.chatroom.ChatRoomRepository;
import com.back.snobs.dto.chatroom.chatmessage.ChatMessageDto;
import com.back.snobs.redisTest.RedisChat;
import com.back.snobs.service.ChatMessageService;
import com.back.snobs.service.ChatMessageServiceRdb;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.*;

@SpringBootTest
class RedisTest {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ChatMessageService chatMessageService;

    @Autowired
    ChatMessageServiceRdb chatMessageServiceRdb;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Test
    void setHashOps() {
        // given
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        String key = "hashKey";

//        // when
//        for(int i = 0; i <= 100; i++) {
//            hashOperations.put(key, UUID.randomUUID().toString(), "Abcd");
//        }

        // then
        String value = hashOperations.get(key, "hello");
        Map<String, String> entries = hashOperations.entries(key);
        for (String o : entries.keySet()) {
            String v = entries.get(o);
            System.out.println(o + " : " + v);
        }

        entries = hashOperations.entries(key);
        for (String o : entries.keySet()) {
            String v = entries.get(o);
            System.out.println(o + " : " + v);
        }
    }

    @Test
    void zSetOps() {
        ZSetOperations<String, RedisChat> zSetOperations = redisTemplate.opsForZSet();
        for(int i = 0; i <= 100; i++) {
            long now = System.currentTimeMillis();
            RedisChat redisChat = new RedisChat(UUID.randomUUID().toString(), now);
            zSetOperations.add("abc", redisChat, now);
        }

        Set<RedisChat> set = zSetOperations.range("abc", 0, -1);
        Iterator<RedisChat> iter = set.iterator();
        int i = 1;
        while(iter.hasNext()) {
            System.out.println(i++ + " ");
            RedisChat rc = iter.next();
            System.out.println(rc.getRoomIdx() + " " + rc.getMessage());
        }

        zSetOperations.removeRange("abc", 0, -1);
    }

    @Test
    void someTest() {
        ZSetOperations<String, RedisChat> zSetOperations = redisTemplate.opsForZSet();
        Set<RedisChat> set = zSetOperations.range("CHATROOM32758", 0, -1);
    }

    @Test
    void someTest2() {
        chatMessageServiceRdb.getMessage(37158L);
    }

    @Test
    void insertMessages() {
        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .chatRoomIdx(5L)
                .message("asdf")
                .userIdx("8d9ef0a4-239f-4ec9-bb99-ea516325cae2")
                .createDate(1L)
                .build();
        for(int i = 0; i < 100000; i++) {
            chatMessageService.saveMessage(chatMessageDto);
        }
    }

    @Test
    void insertMessageUsingRedis() {
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        List<Long> arr = Arrays.asList(32758L, 38878L);
        for (Long idx: arr) {
            for (int i = 0; i <= 100000; i++) {
                ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                        .chatRoomIdx(idx)
                        .message("asdf")
                        .userIdx("7a450833-f07d-4336-afbe-6dc75ce47178")
                        .createDate(System.currentTimeMillis())
                        .build();
                chatMessageService.saveMessage(chatMessageDto);
            }
        }
    }

    @Test
    void insertMessageUsingRdb() {
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        List<Long> arr = Arrays.asList(32758L, 38878L);
        for (Long idx : arr) {
            for (int i = 0; i <= 100000; i++) {
                ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                        .chatRoomIdx(idx)
                        .message("asdf")
                        .userIdx("7a450833-f07d-4336-afbe-6dc75ce47178")
                        .createDate(System.currentTimeMillis())
                        .build();
                chatMessageServiceRdb.saveMessage(chatMessageDto);
            }
        }
    }
}
