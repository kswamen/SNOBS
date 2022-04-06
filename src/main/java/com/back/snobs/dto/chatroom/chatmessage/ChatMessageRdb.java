package com.back.snobs.dto.chatroom.chatmessage;

import com.back.snobs.dto.BaseTimeEntity;
import com.back.snobs.dto.chatroom.ChatRoom;
import com.back.snobs.dto.snob.Snob;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Getter
@RequiredArgsConstructor
@Entity
public class ChatMessageRdb extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageIdx;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    @JoinColumn(name = "chatRoomIdx")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userEmail")
    private Snob snob;

    @Column
    private String message;

    @Builder
    public ChatMessageRdb(ChatRoom chatRoom, Snob snob, String message) {
        this.chatRoom = chatRoom;
        this.snob = snob;
        this.message = message;
    }
}
