package com.back.snobs.service;

import com.back.snobs.dto.chatroom.ChatRoom;
import com.back.snobs.dto.chatroom.ChatRoomRepository;
import com.back.snobs.dto.chatroom.chatmessage.ChatMessageDto;
import com.back.snobs.dto.chatroom.chatmessage.ChatMessageRdb;
import com.back.snobs.dto.chatroom.chatmessage.ChatMessageRepositoryRdb;
import com.back.snobs.dto.snob.Snob;
import com.back.snobs.dto.snob.SnobRepository;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.error.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
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
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getChatRoomIdx()).orElseThrow();
        Snob snob = snobRepository.findBySnobIdx(chatMessageDto.getUserIdx()).orElseThrow();

        ChatMessageRdb chatMessageRdb = chatMessageRepositoryRdb.save(ChatMessageRdb.builder()
                .chatRoom(chatRoom)
                .snob(snob)
                .message(chatMessageDto.getMessage()).build());

        long now = System.currentTimeMillis();
        chatMessageDto.setCreateDate(now);

        return chatMessageDto;
    }

    public ResponseEntity<CustomResponse> getMessage(Long chatRoomIdx) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomIdx).orElseThrow();
        List<ChatMessageRdb> chatMessageRdbList = chatMessageRepositoryRdb.findTop1000ByChatRoomOrderByCreateDateDesc(chatRoom);
        Collections.reverse(chatMessageRdbList);
        List<ChatMessageDto> tempList = new ArrayList<>();
        for(ChatMessageRdb chatMessageRdb: chatMessageRdbList) {
            ChatMessageDto chatMessageDto = new ChatMessageDto();
            chatMessageDto.setUserIdx(chatMessageRdb.getSnob().getSnobIdx());
            chatMessageDto.setMessage(chatMessageRdb.getMessage());
            chatMessageDto.setCreateDate(chatMessageRdb.getCreateDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            tempList.add(chatMessageDto);
        }

        return new ResponseEntity<>(new CustomResponse(ResponseCode.SUCCESS, tempList), HttpStatus.valueOf(200));
    }
}
