package com.back.snobs.batch;

import com.back.snobs.domain.chatroom.ChatRoom;
import com.back.snobs.domain.chatroom.chatmessage.ChatMessage;
import com.back.snobs.domain.chatroom.chatmessage.ChatMessageRdb;
import com.back.snobs.domain.log.Log;
import com.back.snobs.domain.reaction.Reaction;
import com.back.snobs.domain.snob.Snob;
import com.back.snobs.service.chatmessage.ChatMessageService;
import com.back.snobs.service.ChatRoomService;
import com.back.snobs.service.DailyLogService;
import com.back.snobs.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
@RequiredArgsConstructor
public class SnobsScheduler {
    private final DailyLogService dailyLogService;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private static final int MAX_LOG = 3;
    private final RedisTemplate redisTemplate;
    private ZSetOperations<String, ChatMessage> zSetOperations;

    @PostConstruct
    private void init() {
        zSetOperations = redisTemplate.opsForZSet();
    }


    // Redis에 저장된 데이터를 MySQL에 영속화시키지 않도록 바뀌면서
    // 더 이상 사용되지 않는 메소드임
//    @Scheduled(fixedDelay = 3600000)
    public void RedisUpdate() {
        // 전체 채팅방을 불러오는 건 코스트가 너무 크다.
//        List<ChatRoom> chatRoomList = chatRoomService.findAll();
        List<ChatRoom> chatRoomList = chatRoomService.findTop300();
        // 실제 채팅방 인덱스를 리스트 인덱스에 매핑
        Map<Long, Integer> chatRoomIdxMap = new HashMap<>();
        for(int i = 0; i < chatRoomList.size(); i++) {
            chatRoomIdxMap.put(chatRoomList.get(i).getChatRoomIdx(), i);
        }

        try (RedisConnection redisConnection = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection()) {
            ScanOptions options = ScanOptions.scanOptions().match("CHATROOM*").count(10).build();
            Cursor<byte[]> cursor = redisConnection.scan(options);
            StringBuilder key = new StringBuilder();
            List<ChatMessageRdb> messageToSave = new ArrayList<>();
            // JPA messages bulkinsert
            // JPA ChatRoom bulkUpdate
            // 모든 key에 대해서 연산 수행
            // 연산이 종료되면 1000개 데이터만 남기고 나머지는 제거
            // Set<String> keys = redisTemplate.keys("CHATROOM*");
            while (cursor.hasNext()) {
                key.setLength(0);
                key.append(new String(cursor.next()));
                System.out.println(key);
                Long chatRoomIdx = convertKey(key);
                int listIdx = chatRoomIdxMap.get(chatRoomIdx);
                Long lastMessageSaved = chatRoomList.get(listIdx).getLastMessageSaved();

                // 거꾸로 읽음(최근에 입력한 메시지가 먼저 오게)
                Set<ChatMessage> messages = zSetOperations.reverseRange(RedisUtils.getChatRoomKey(chatRoomIdx), 0, -1);
                long finalDateTime = -1L;
                assert messages != null;
                // 레디스 메시지들 중 마지막 메시지 삽입 시간보다
                // 이후에 기록된 메시지들을 골라내는 작업
                for (ChatMessage message : messages) {
                    finalDateTime = Long.max(finalDateTime, message.getCreateDate());
                    if (message.getCreateDate() > lastMessageSaved) {
                        ChatMessageRdb chatMessageRdb = ChatMessageRdb.builder()
                                .chatRoomIdx(chatRoomIdx)
                                .userIdx(message.getUserIdx())
                                .message(message.getMessage())
                                .createDate(message.getCreateDate())
                                .build();
                        messageToSave.add(chatMessageRdb);
                    } else break;
                }
                // Bulk update를 하기 위해 마지막으로 메시지가 삽입된 시각 업데이트
                chatRoomList.get(listIdx).setLastMessageSaved(finalDateTime);
                // 1,000 개의 메시지만 남기고 나머지는 evict
                if (messages.size() > 1000) {
                    zSetOperations.removeRange(RedisUtils.getChatRoomKey(chatRoomIdx), 0, messages.size() - 1001);
                }
            }

            chatMessageService.saveAllByJDBC(messageToSave);
            chatRoomService.updateAllByJDBC(chatRoomList);
            System.out.println("Done!");
        }
    }

    private Long convertKey(StringBuilder key) {
        return Long.parseLong(key.substring(8));
    }

//    @Scheduled(cron = "0 0 * * * *")
//    @Scheduled(cron = "0 0 0 * * *")
    @Scheduled(fixedDelay = 36000000)
    public void scheduledTask() {
        dailyLogService.deleteAllDailyLogInBatch();
        LocalDateTime now = LocalDateTime.now();

//        // 3일이 지난 리액션 삭제
//        List<Reaction> reactionList = dailyLogService.findAllReactions();
//        for(Reaction reaction: reactionList) {
//            LocalDateTime dt = reaction.getCreateDate();
//            if(ChronoUnit.DAYS.between(dt, now) > 3) {
//                dailyLogService.deleteReaction(reaction);
//            }
//        }
//
//        // 3일이 지난 채팅방 삭제
//        List<ChatRoom> chatRoomList = dailyLogService.findAllChatRooms();
//        for(ChatRoom chatRoom: chatRoomList) {
//            LocalDateTime dt = chatRoom.getCreateDate();
//            if(ChronoUnit.DAYS.between(dt, now) > 3) {
//                dailyLogService.deleteChatRoom(chatRoom);
//                zSetOperations.remove(RedisUtils.getChatRoomKey(chatRoom.getChatRoomIdx()));
//            }
//        }

        List<Snob> snobList = dailyLogService.findAllSnobs();
        List<Log> logList = dailyLogService.findAllLogs();
        Collections.shuffle(logList);

        // 그날의 로그 추천 목록 생성
        for (Snob snob: snobList) {
            int logCnt = 0;
            for (Log log: logList) {
                if (log.getSnob().getUserEmail().equals(snob.getUserEmail())) continue;
                if (!log.getIsPublic()) continue;
                if (logCnt >= MAX_LOG) break;
                if (dailyLogService.createDailyLog(snob, log)) {
                    logCnt += 1;
                }
            }
        }
    }
}
