package com.back.snobs.service;

import com.back.snobs.dto.chatroom.ChatRoom;
import com.back.snobs.dto.chatroom.ChatRoomRepository;
import com.back.snobs.dto.chatroom.chatmessage.ChatMessage;
import com.back.snobs.dto.chatroom.chatmessage.ChatMessageDto;
import com.back.snobs.dto.chatroom.chatmessage.ChatMessageRdb;
import com.back.snobs.dto.chatroom.chatmessage.ChatMessageRepositoryRdb;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.error.ResponseCode;
import com.back.snobs.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatMessageService implements ChatMessageServiceInterface{
    private final RedisTemplate redisTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepositoryRdb chatMessageRepositoryRdb;
    private final JdbcTemplate jdbcTemplate;
    private final int batchSize = 500;
    private ZSetOperations<String, ChatMessage> zSetOperations;

    @PostConstruct
    private void init() {
        zSetOperations = redisTemplate.opsForZSet();
    }

    public ChatMessageDto saveMessage(ChatMessageDto chatMessageDto) {
        long now = System.currentTimeMillis();
        chatMessageDto.setCreateDate(now);
        ChatMessage chatMessage = chatMessageDto.toEntity();
        zSetOperations.add(RedisUtils.getChatRoomKey(chatMessage.getChatRoomIdx()), chatMessage, now);
        return chatMessageDto;
    }

    public void saveAllByJDBC(List<ChatMessageRdb> chatMessageRdbList) {
        int batchCount = 0;
        List<ChatMessageRdb> subItems = new ArrayList<>();
        for (int i = 0; i < chatMessageRdbList.size(); i++) {
            subItems.add(chatMessageRdbList.get(i));
            if ((i + 1) % batchSize == 0) {
                batchCount = batchInsert(batchSize, batchCount, subItems);
            }
        }
        if (!subItems.isEmpty()) {
            batchCount = batchInsert(batchSize, batchCount, subItems);
        }
        System.out.println("batchCount = " + batchCount);
    }

    private int batchInsert(int batchSize, int batchCount, List<ChatMessageRdb> atMsgs) {
        String query = "INSERT INTO CHAT_MESSAGE_RDB (`create_date`, `chat_room_idx`, `message`, `user_idx`)"
                + " VALUES (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, atMsgs.get(i).getCreateDate());
                ps.setLong(2, atMsgs.get(i).getChatRoomIdx());
                ps.setString(3, atMsgs.get(i).getMessage());
                ps.setString(4, atMsgs.get(i).getUserIdx());
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

    public ResponseEntity<CustomResponse> getMessage(Long chatRoomIdx) {
        Set<ChatMessage> messages;

        // Redis에 키가 존재하지 않으면 RDB에서 읽어와서 저장
        if (!redisTemplate.hasKey(RedisUtils.getChatRoomKey(chatRoomIdx))) {
            ChatRoom chatRoom = chatRoomRepository.findById(chatRoomIdx).orElseThrow();
            List<ChatMessageRdb> chatMessageRdbList = chatMessageRepositoryRdb.findTop1000ByChatRoomIdxOrderByCreateDateDesc(chatRoomIdx);
//            Collections.reverse(chatMessageRdbList);
            for(ChatMessageRdb chatMessageRdb: chatMessageRdbList) {
                ChatMessageDto chatMessageDto = new ChatMessageDto();
                chatMessageDto.setUserIdx(chatMessageRdb.getUserIdx());
                chatMessageDto.setMessage(chatMessageRdb.getMessage());
                chatMessageDto.setCreateDate(chatMessageRdb.getCreateDate());

                ChatMessage chatMessage = chatMessageDto.toEntity();
                zSetOperations.add(RedisUtils.getChatRoomKey(chatRoomIdx), chatMessage, chatMessage.getCreateDate());
            }
        }
        messages = zSetOperations.range(RedisUtils.getChatRoomKey(chatRoomIdx), -1000, -1);
        assert messages != null;
        List<ChatMessage> result = new ArrayList<>(messages);
        return new ResponseEntity<>(new CustomResponse(ResponseCode.SUCCESS, result), HttpStatus.valueOf(200));
    }
}
