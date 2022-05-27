package com.back.snobs;

import com.back.snobs.domain.chatroom.ChatRoom;
import com.back.snobs.domain.chatroom.ChatRoomRepository;
import com.back.snobs.domain.chatroom.chatmessage.ChatMessageDto;
import com.back.snobs.domain.chatroom.chatmessage.ChatMessageRdb;
import com.back.snobs.domain.chatroom.chatmessage.ChatMessageRepositoryRdb;
import com.back.snobs.domain.snob.SnobRepository;
import com.back.snobs.service.ChatMessageService;
import com.back.snobs.service.ChatMessageServiceRdb;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

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
    ChatMessageRepositoryRdb chatMessageRepositoryRdb;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    SnobRepository snobRepository;

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
    void someTest2() {
        chatMessageServiceRdb.getMessage(37158L);
    }

    @Test
    void insertMessages() throws InterruptedException {
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
//        Snob a = snobRepository.findBySnobIdx("016f0fa0-6d8f-4bb2-a03c-aa76ab551b5e").orElse(null);
//        Snob b = snobRepository.findBySnobIdx("79f72adc-fdfc-4044-a855-f1a27c720ee2").orElse(null);
//        for (int i = 0; i <= 100000; i++) {
//            chatRoomRepository.save(ChatRoom.builder()
//                    .senderSnob(a)
//                    .receiverSnob(b)
//                    .build());
//        }

        List<String> userIdx = Arrays.asList("79f72adc-fdfc-4044-a855-f1a27c720ee2", "016f0fa0-6d8f-4bb2-a03c-aa76ab551b5e");
        for (ChatRoom chatRoom : chatRoomList) {
            for (int i = 0; i <= 1000; i++) {
                int idx = i % 2;
                if (idx == 0) {
                    chatMessageRepositoryRdb.save(ChatMessageRdb.builder()
                            .chatRoomIdx(chatRoom.getChatRoomIdx())
                            .message("Message From user 1")
                            .createDate(System.currentTimeMillis())
                            .userIdx(userIdx.get(idx))
                            .build());
                }
                else {
                    chatMessageRepositoryRdb.save(ChatMessageRdb.builder()
                            .chatRoomIdx(chatRoom.getChatRoomIdx())
                            .message("Message From user 2")
                            .createDate(System.currentTimeMillis())
                            .userIdx((userIdx.get(idx)))
                            .build());
                }
                Thread.sleep(1);
            }
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
