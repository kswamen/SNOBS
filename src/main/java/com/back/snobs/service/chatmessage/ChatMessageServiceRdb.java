package com.back.snobs.service.chatmessage;

import com.back.snobs.domain.chatroom.ChatRoomRepository;
import com.back.snobs.domain.chatroom.chatmessage.ChatMessageDto;
import com.back.snobs.domain.chatroom.chatmessage.ChatMessageRdb;
import com.back.snobs.domain.chatroom.chatmessage.ChatMessageRepositoryRdb;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.error.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceRdb implements ChatMessageServiceInterface {
    private final ChatMessageRepositoryRdb chatMessageRepositoryRdb;
    private final ChatRoomRepository chatRoomRepository;

//    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public ChatMessageDto saveMessage(ChatMessageDto chatMessageDto) {
        long prev = System.currentTimeMillis();
        chatMessageRepositoryRdb.save(ChatMessageRdb.builder()
                .chatRoomIdx(chatMessageDto.getChatRoomIdx())
                .userIdx(chatMessageDto.getUserIdx())
                .message(chatMessageDto.getMessage()).build());
        long now = System.currentTimeMillis();
        chatMessageDto.setCreateDate(now);
        System.out.println("수행시간: " + (int) (now - prev));
        return chatMessageDto;
    }

    public ResponseEntity<CustomResponse> getMessage(Long chatRoomIdx) {
        chatRoomRepository.findById(chatRoomIdx).orElseThrow();
        List<ChatMessageRdb> chatMessageRdbList = chatMessageRepositoryRdb.findTop1000ByChatRoomIdxOrderByChatMessageIdxDesc(chatRoomIdx);
        Collections.reverse(chatMessageRdbList);
        List<ChatMessageDto> tempList = new ArrayList<>();
        for(ChatMessageRdb chatMessageRdb: chatMessageRdbList) {
            ChatMessageDto chatMessageDto = new ChatMessageDto();
            chatMessageDto.setUserIdx(chatMessageRdb.getUserIdx());
            chatMessageDto.setMessage(chatMessageRdb.getMessage());
            chatMessageDto.setCreateDate(chatMessageRdb.getCreateDate());
            tempList.add(chatMessageDto);
        }

        return new ResponseEntity<>(new CustomResponse(ResponseCode.SUCCESS, tempList), HttpStatus.valueOf(200));
    }
}
