package com.back.snobs.dto.chatroom.chatmessage;

import com.back.snobs.dto.chatroom.ChatRoom;
import com.back.snobs.dto.chatroom.chatmessage.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepositoryRdb extends JpaRepository<ChatMessageRdb, Long> {
    List<ChatMessageRdb> findTop5000ByChatRoomOrderByCreateDateDesc(ChatRoom chatRoom);
}