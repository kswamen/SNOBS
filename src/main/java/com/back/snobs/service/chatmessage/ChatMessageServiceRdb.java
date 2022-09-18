package com.back.snobs.service.chatmessage;

import com.back.snobs.domain.chatroom.ChatRoom;
import com.back.snobs.domain.chatroom.ChatRoomRepository;
import com.back.snobs.domain.chatroom.chatmessage.ChatMessageDto;
import com.back.snobs.domain.chatroom.chatmessage.ChatMessageRdb;
import com.back.snobs.domain.chatroom.chatmessage.ChatMessageRepositoryRdb;
import com.back.snobs.domain.snob.SnobRepository;
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
public class ChatMessageServiceRdb implements ChatMessageServiceInterface{
    private final ChatMessageRepositoryRdb chatMessageRepositoryRdb;
    private final ChatRoomRepository chatRoomRepository;
    private final SnobRepository snobRepository;

    public ChatMessageDto saveMessage(ChatMessageDto chatMessageDto) {
        ChatMessageRdb chatMessageRdb = chatMessageRepositoryRdb.save(ChatMessageRdb.builder()
                .chatRoomIdx(chatMessageDto.getChatRoomIdx())
                .userIdx(chatMessageDto.getUserIdx())
                .message(chatMessageDto.getMessage()).build());

        long now = System.currentTimeMillis();
        chatMessageDto.setCreateDate(now);

        return chatMessageDto;
    }

    public ResponseEntity<CustomResponse> getMessage(Long chatRoomIdx) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomIdx).orElseThrow();
        List<ChatMessageRdb> chatMessageRdbList = chatMessageRepositoryRdb.findTop1000ByChatRoomIdxOrderByCreateDateDesc(chatRoomIdx);
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
