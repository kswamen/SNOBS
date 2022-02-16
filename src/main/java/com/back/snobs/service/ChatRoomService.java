package com.back.snobs.service;

import com.back.snobs.dto.chatroom.ChatRoom;
import com.back.snobs.dto.chatroom.ChatRoomRepository;
import com.back.snobs.dto.reaction.Reaction;
import com.back.snobs.dto.reaction.ReactionRepository;
import com.back.snobs.dto.snob.Snob;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.error.ResponseCode;
import com.back.snobs.error.exception.ChatRoomDuplicationException;
import com.back.snobs.error.exception.NoDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ReactionRepository reactionRepository;

    public ResponseEntity<CustomResponse> getChatRoom(String userEmail) {
        List<ChatRoom> cr1 = chatRoomRepository.findAllChatRoomByMeWithFetchJoin(userEmail);
        List<ChatRoom> cr2 = chatRoomRepository.findAllChatRoomByYouWithFetchJoin(userEmail);
        List<ChatRoom> result = new ArrayList<>();
        result.addAll(cr1);
        result.addAll(cr2);

        return new ResponseEntity<>(new CustomResponse(ResponseCode.SUCCESS, result), HttpStatus.valueOf(200));
    }

    // Chatroom create
    @Transactional
    public ResponseEntity<CustomResponse> createChatRoom(Long reactionIdx) {
        Reaction reaction = reactionRepository.findById(reactionIdx).orElseThrow(() ->
                new NoDataException("No Such Data", ResponseCode.DATA_NOT_FOUND));
        Snob receiverSnob = reaction.getReceiverSnob();
        Snob senderSnob = reaction.getSenderSnob();
        if(chatRoomRepository.existsBySenderSnob_userEmailAndReceiverSnob_userEmail(senderSnob.getUserEmail(), receiverSnob.getUserEmail()) ||
                chatRoomRepository.existsBySenderSnob_userEmailAndReceiverSnob_userEmail(receiverSnob.getUserEmail(), senderSnob.getUserEmail())) {
            throw new ChatRoomDuplicationException("ChatRoom Duplicated", ResponseCode.CHATROOM_DUPLICATION);
        }

        return new ResponseEntity<>(new CustomResponse(ResponseCode.SUCCESS, chatRoomRepository.save(
                ChatRoom.builder()
                        .receiverSnob(receiverSnob)
                        .senderSnob(senderSnob)
                        .build()
        )), HttpStatus.valueOf(200));
    }
}
