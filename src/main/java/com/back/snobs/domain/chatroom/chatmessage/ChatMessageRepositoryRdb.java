package com.back.snobs.domain.chatroom.chatmessage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepositoryRdb extends JpaRepository<ChatMessageRdb, Long> {
    List<ChatMessageRdb> findTop1000ByChatRoomIdxOrderByCreateDateDesc(Long chatRoomIdx);
}