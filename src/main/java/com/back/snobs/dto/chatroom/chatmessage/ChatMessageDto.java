package com.back.snobs.dto.chatroom.chatmessage;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatMessageDto {
    private Long chatRoomIdx;
    private String message;
    private String userIdx;
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
    public ChatMessageDto(Long chatRoomIdx, String message, String userIdx, Long createDate) {
        this.chatRoomIdx = chatRoomIdx;
        this.message = message;
        this.userIdx = userIdx;
        this.createDate = createDate;
    }
}
