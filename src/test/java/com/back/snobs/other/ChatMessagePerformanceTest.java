package com.back.snobs.other;

import com.back.snobs.domain.chatroom.ChatRoom;
import com.back.snobs.domain.chatroom.ChatRoomRepository;
import com.back.snobs.domain.chatroom.chatmessage.ChatMessageRdb;
import com.back.snobs.domain.chatroom.chatmessage.ChatMessageRepositoryRdb;
import com.back.snobs.service.chatmessage.ChatMessageService;
import com.back.snobs.service.chatmessage.ChatMessageServiceRdb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
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

//    @Test
    // 23.183 sec 소요(No Index)
    // 243 ms 소요(chat_room_idx, create_date 복합 인덱스 적용)
    // 202 ms 소요(chat_room_idx 인덱스 적용)

    // 연속 두 번 실행 시, 두 번째 실행에서 12ms 소요(chat_room_idx 인덱스 적용)
    // 연속 두 번 실행 시, 두 번째 실행에서 10 sec 소요(No Index)
    void testPerformanceWithRdbms() {
        long l1 = System.currentTimeMillis();
        chatMessageServiceRdb.getMessage(2L);
        long l2 = System.currentTimeMillis();
        System.out.println("First Test - " + (l2 - l1));

        l1 = System.currentTimeMillis();
        chatMessageServiceRdb.getMessage(2L);
        l2 = System.currentTimeMillis();
        System.out.println("Second Test - " + (l2 - l1));
    }

//    @Test
    // 912 ms 소요(key 방출됨, 영속화 DB에 복합 인덱스 적용됨)
    // 806 ms 소요(key 존재함, 영속화 DB에 복합 인덱스 적용됨)
    // 841 ms 소요(key 존재함, 영속화 DB에 인덱스 적용되지 않음)
    // 25.422 sec 소요(key 방출됨, 영속화 DB에 인덱스 적용되지 않음)
    // 1.11 sec 소요(key 방출됨, 영속화 DB에 chat_room_idx 인덱스 적용)
    // 826 ms 소요(key 존재함, 영속화 DB에 chat_room_idx 인덱스 적용)

    // 연속 두 번 실행 시, 두 번째 실행에서 18ms 소요(MySQL과 연동 안함)
    void testPerformanceWithRedis() {
        long l1 = System.currentTimeMillis();
        chatMessageService.getMessage(2L);
        long l2 = System.currentTimeMillis();
        System.out.println("First Test - " + (l2 - l1));

        l1 = System.currentTimeMillis();
        chatMessageService.getMessage(2L);
        l2 = System.currentTimeMillis();
        System.out.println("Second Test - " + (l2 - l1));
    }



//    @Test
    void insertDummyData() {
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();

        List<ChatMessageRdb> list = new ArrayList<>();
        Random r = new Random();
        for (ChatRoom chatRoom : chatRoomList) {
            long idx;
            int x = r.nextInt(2);
            if (x == 0) {
                idx = 1L;
            }
            else {
                idx = 2L;
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
