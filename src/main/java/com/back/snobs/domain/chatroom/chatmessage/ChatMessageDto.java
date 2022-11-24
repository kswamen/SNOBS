package com.back.snobs.domain.chatroom.chatmessage;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatMessageDto {
    private Long chatRoomIdx;
    private String message;
    private Long userIdx;
    private Long createDate;

    public ChatMessage toEntity() {
        return ChatMessage.builder()
                .chatRoomIdx(chatRoomIdx)
                .userIdx(userIdx)
                .createDate(createDate)
                .message(message)
                .build();
    }

    @Builder
    public ChatMessageDto(Long chatRoomIdx, String message, Long userIdx, Long createDate) {
        this.chatRoomIdx = chatRoomIdx;
        this.message = message;
        this.userIdx = userIdx;
        this.createDate = createDate;
    }
}
