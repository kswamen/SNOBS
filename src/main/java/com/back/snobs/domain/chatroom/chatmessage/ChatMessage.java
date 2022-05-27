package com.back.snobs.domain.chatroom.chatmessage;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class ChatMessage implements Serializable {
    private Long chatRoomIdx;
    private String message;
    private String userIdx;
    private Long createDate;

    @Builder
    public ChatMessage(Long chatRoomIdx, String message, String userIdx, Long createDate) {
        this.chatRoomIdx = chatRoomIdx;
        this.message = message;
        this.userIdx = userIdx;
        this.createDate = createDate;
    }
}
