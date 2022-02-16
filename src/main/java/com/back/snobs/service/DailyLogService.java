package com.back.snobs.service;

import com.back.snobs.dto.chatroom.ChatRoom;
import com.back.snobs.dto.chatroom.ChatRoomRepository;
import com.back.snobs.dto.log.Log;
import com.back.snobs.dto.log.LogRepository;
import com.back.snobs.dto.reaction.Reaction;
import com.back.snobs.dto.reaction.ReactionRepository;
import com.back.snobs.dto.snob.Snob;
import com.back.snobs.dto.snob.SnobRepository;
import com.back.snobs.dto.snob.dailyLog.DailyLog;
import com.back.snobs.dto.snob.dailyLog.DailyLogId;
import com.back.snobs.dto.snob.dailyLog.DailyLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyLogService {
    private final DailyLogRepository dailyLogRepository;
    private final SnobRepository snobRepository;
    private final LogRepository logRepository;
    private final ReactionRepository reactionRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public boolean createDailyLog(Snob snob, Log log) {
        Optional<DailyLog> dailyLog = dailyLogRepository.findById(new DailyLogId(
                snob.getUserEmail(), log.getLogIdx()
        ));
        if (dailyLog.isPresent()) return false;
        else {
            dailyLogRepository.save(DailyLog.builder()
                    .snob(snob)
                    .log(log)
                    .build());
            return true;
        }
    }

    public List<Snob> findAllSnobs() {
        return snobRepository.findAll();
    }

    public List<Log> findAllLogs() {
        return logRepository.findAll();
    }

    public List<Reaction> findAllReactions() {
        return reactionRepository.findAll();
    }

    public void deleteReaction(Reaction reaction) {
        reactionRepository.delete(reaction);
    }

    public List<ChatRoom> findAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    public void deleteChatRoom(ChatRoom chatRoom) {
        chatRoomRepository.delete(chatRoom);
    }

    public List<DailyLog> findAllDailyLogs() {
        return dailyLogRepository.findAll();
    }

    @Transactional
    public void deleteAllDailyLogInBatch() {
        dailyLogRepository.deleteAllInBatch();
    }
}
