package com.back.snobs.dto.chatroom;

import com.back.snobs.dto.BaseTimeEntity;
import com.back.snobs.dto.snob.Snob;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Getter
@RequiredArgsConstructor
@Entity
public class ChatRoom extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Snob receiverSnob;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Snob senderSnob;

    @Builder
    public ChatRoom(Snob receiverSnob, Snob senderSnob) {
        this.receiverSnob = receiverSnob;
        this.senderSnob = senderSnob;
    }
}
