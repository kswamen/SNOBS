package com.back.snobs.batch;

import com.back.snobs.dto.chatroom.ChatRoom;
import com.back.snobs.dto.chatroom.ChatRoomRepository;
import com.back.snobs.dto.chatroom.chatmessage.ChatMessage;
import com.back.snobs.dto.chatroom.chatmessage.ChatMessageRdb;
import com.back.snobs.dto.chatroom.chatmessage.ChatMessageRepositoryRdb;
import com.back.snobs.dto.log.Log;
import com.back.snobs.dto.reaction.Reaction;
import com.back.snobs.dto.snob.Snob;
import com.back.snobs.dto.snob.SnobRepository;
import com.back.snobs.service.ChatMessageService;
import com.back.snobs.service.ChatRoomService;
import com.back.snobs.service.DailyLogService;
import com.back.snobs.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    @Scheduled(fixedDelay = 3600000)
    public void RedisUpdate() {
        List<ChatRoom> chatRoomList = chatRoomService.findAll();
        // 실제 채팅방 인덱스를 리스트 인덱스에 매핑
        Map<Long, Integer> chatRoomIdxMap = new HashMap<>();
        for(int i = 0; i < chatRoomList.size(); i++) {
            chatRoomIdxMap.put(chatRoomList.get(i).getChatRoomIdx(), i);
        }

        List<ChatMessageRdb> messageToSave = new ArrayList<>();
        // JPA messages bulkinsert
        // JPA ChatRoom bulkUpdate
        // 모든 key에 대해서 연산 수행
        // 연산이 종료되면 1000개 데이터만 남기고 나머지는 제거
        Set<String> keys = redisTemplate.keys("CHATROOM*");
        for (String key : keys) {
            Long chatRoomIdx = convertKey(key);
            int listIdx = chatRoomIdxMap.get(chatRoomIdx);
            Long lastMessageSaved = chatRoomList.get(listIdx).getLastMessageSaved();
            System.out.println("lastMessageSaved = " + lastMessageSaved);

            Set<ChatMessage> messages = zSetOperations.range(RedisUtils.getChatRoomKey(chatRoomIdx), 0, -1);
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
                }
            }
            // Bulk update를 하기 위해 마지막으로 메시지가 삽입된 시각 업데이트
            chatRoomList.get(listIdx).setLastMessageSaved(finalDateTime);
            // 1,000 개의 메시지만 남기고 나머지는 evict
            if (messages.size() > 1000) {
                zSetOperations.removeRange(RedisUtils.getChatRoomKey(chatRoomIdx), 0, messages.size() - 1000);
            }
        }

        chatMessageService.saveAllByJDBC(messageToSave);
        chatRoomService.updateAllByJDBC(chatRoomList);
        System.out.println("Done!");
    }

    private Long convertKey(String key) {
        return Long.parseLong(key.substring(8));
    }

//    @Scheduled(cron = "0 0 * * * *")
//    @Scheduled(cron = "0 0 0 * * *")
//    @Scheduled(fixedDelay = 36000000)
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
                if (logCnt >= 3) break;
                if (dailyLogService.createDailyLog(snob, log)) {
                    logCnt += 1;
                }
            }
        }
    }
}
