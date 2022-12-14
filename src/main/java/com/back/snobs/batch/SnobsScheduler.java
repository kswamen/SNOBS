package com.back.snobs.batch;

import com.back.snobs.domain.chatroom.ChatRoom;
import com.back.snobs.domain.log.Log;
import com.back.snobs.domain.reaction.Reaction;
import com.back.snobs.domain.snob.Snob;
import com.back.snobs.service.ChatRoomService;
import com.back.snobs.service.DailyLogService;
import com.back.snobs.service.chatmessage.ChatMessageService;
import com.back.snobs.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SnobsScheduler {
    private final DailyLogService dailyLogService;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private static final int MAX_LOG = 3;
    private final RedisTemplate<String, Object> redisTemplate;
    private ListOperations<String, Object> listOperations;

    @PostConstruct
    private void init() {
        listOperations = redisTemplate.opsForList();
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

        // 3일이 지난 리액션 삭제
        List<Reaction> reactionList = dailyLogService.findAllReactions();
        for(Reaction reaction: reactionList) {
            LocalDateTime dt = reaction.getCreateDate();
            if(ChronoUnit.DAYS.between(dt, now) > 3) {
                dailyLogService.deleteReaction(reaction);
            }
        }

        // 3일이 지난 채팅방 삭제
        List<ChatRoom> chatRoomList = dailyLogService.findAllChatRooms();
        for(ChatRoom chatRoom: chatRoomList) {
            LocalDateTime dt = chatRoom.getCreateDate();
            if(ChronoUnit.DAYS.between(dt, now) > 3) {
                dailyLogService.deleteChatRoom(chatRoom);
                redisTemplate.delete(RedisUtils.getChatRoomKey(chatRoom.getChatRoomIdx()));
            }
        }

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
