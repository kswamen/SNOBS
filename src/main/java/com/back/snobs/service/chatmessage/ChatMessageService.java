package com.back.snobs.service.chatmessage;

import com.back.snobs.domain.chatroom.chatmessage.ChatMessage;
import com.back.snobs.domain.chatroom.chatmessage.ChatMessageDto;
import com.back.snobs.domain.chatroom.chatmessage.ChatMessageRdb;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.error.ResponseCode;
import com.back.snobs.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService implements ChatMessageServiceInterface{
    private final RedisTemplate<String, Object> redisTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final int batchSize = 500;
    private ListOperations<String, Object> listOperations;

    @PostConstruct
    private void init() {
        listOperations = redisTemplate.opsForList();
    }

    @Transactional
    public ChatMessageDto saveMessage(ChatMessageDto chatMessageDto) {
        long now = System.currentTimeMillis();
        chatMessageDto.setCreateDate(now);
        ChatMessage chatMessage = chatMessageDto.toEntity();
        listOperations.rightPush(RedisUtils.getChatRoomKey(chatMessage.getChatRoomIdx()), chatMessage);

        return chatMessageDto;
    }

    public ResponseEntity<CustomResponse> getMessage(Long chatRoomIdx) {
        List<Object> messages = listOperations.range(RedisUtils.getChatRoomKey(chatRoomIdx), -1000, -1);
        assert messages != null;
//        List<ChatMessage> result = messages.stream()
//                .map(x -> (ChatMessage) x)
//                .collect(Collectors.toList());

        return new ResponseEntity<>(new CustomResponse(ResponseCode.SUCCESS, messages), HttpStatus.valueOf(200));
    }

    public void saveAllByJDBC(List<ChatMessageRdb> chatMessageRdbList) {
        int batchCount = 0;
        List<ChatMessageRdb> subItems = new ArrayList<>();
        for (int i = 0; i < chatMessageRdbList.size(); i++) {
            subItems.add(chatMessageRdbList.get(i));
            if ((i + 1) % batchSize == 0) {
                batchCount = batchInsert(batchCount, subItems);
            }
        }
        if (!subItems.isEmpty()) {
            batchCount = batchInsert(batchCount, subItems);
        }
        System.out.println("batchCount = " + batchCount);
    }

    private int batchInsert(int batchCount, List<ChatMessageRdb> atMsgs) {
        String query = "INSERT INTO CHAT_MESSAGE_RDB (`create_date`, `chat_room_idx`, `message`, `user_idx`)"
                + " VALUES (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, atMsgs.get(i).getCreateDate());
                ps.setLong(2, atMsgs.get(i).getChatRoomIdx());
                ps.setString(3, atMsgs.get(i).getMessage());
                ps.setLong(4, atMsgs.get(i).getUserIdx());
            }

            @Override
            public int getBatchSize() {
                return atMsgs.size();
            }
        });
        atMsgs.clear();
        batchCount++;
        return batchCount;
    }
}
