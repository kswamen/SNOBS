package com.back.snobs.other;

import com.back.snobs.domain.chatroom.ChatRoom;
import com.back.snobs.domain.chatroom.ChatRoomRepository;
import com.back.snobs.domain.chatroom.chatmessage.ChatMessage;
import com.back.snobs.domain.chatroom.chatmessage.ChatMessageRdb;
import com.back.snobs.domain.chatroom.chatmessage.ChatMessageRepositoryRdb;
import com.back.snobs.domain.snob.Snob;
import com.back.snobs.service.chatmessage.ChatMessageService;
import com.back.snobs.service.chatmessage.ChatMessageServiceRdb;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@SpringBootTest
class ChatMessagePerformanceTest {
    // Message count = 33,554,466
    @Autowired
    ChatMessageService chatMessageService;
    @Autowired
    ChatMessageServiceRdb chatMessageServiceRdb;
    @Autowired
    ChatRoomRepository chatRoomRepository;
    @Autowired
    ChatMessageRepositoryRdb chatMessageRepositoryRdb;

    @Test
    // 23.183 sec 소요(No Index)
    // 243 ms 소요(chat_room_idx, create_date 복합 인덱스 적용)
    // 202 ms 소요(chat_room_idx 인덱스 적용)
    void testPerformanceWithRdbms() {
        chatMessageServiceRdb.getMessage(2L);
    }

    @Test
    // 912 ms 소요(key 방출됨, 영속화 DB에 복합 인덱스 적용됨)
    // 806 ms 소요(key 존재함, 영속화 DB에 복합 인덱스 적용됨)
    // 841 ms 소요(key 존재함, 영속화 DB에 인덱스 적용되지 않음)
    // 25.422 sec 소요(key 방출됨, 영속화 DB에 인덱스 적용되지 않음)
    // 1.11 sec 소요(key 방출됨, 영속화 DB에 chat_room_idx 인덱스 적용)
    // 826 ms 소요(key 존재함, 영속화 DB에 chat_room_idx 인덱스 적용)
    void testPerformanceWithRedis() {
        chatMessageService.getMessage(2L);
    }

    @Test
    void insertDummyData() {
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();

        List<ChatMessageRdb> list = new ArrayList<>();
        Random r = new Random();
        for (ChatRoom chatRoom : chatRoomList) {
            String idx;
            int x = r.nextInt(2);
            if (x == 0) {
                idx = "1caf922d-b72c-4b0c-8b40-75ad52e4890a";
            }
            else {
                idx = "eef19194-aa45-4dec-bba2-3d4926ed7791";
            }
            ChatMessageRdb messageRdb = ChatMessageRdb.builder()
                    .chatRoomIdx(chatRoom.getChatRoomIdx())
                    .message("Hello There!")
                    .createDate(System.currentTimeMillis())
                    .userIdx(idx)
                    .build();
            list.add(messageRdb);
        }

        chatMessageRepositoryRdb.saveAll(list);
    }
}
