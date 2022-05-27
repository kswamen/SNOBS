package com.back.snobs.domain.chatroom.chatmessage;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Getter
@RequiredArgsConstructor
@Entity
public class ChatMessageRdb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageIdx;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    @JsonIgnore
//    @JoinColumn(name = "chatRoomIdx")
    private Long chatRoomIdx;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userEmail")
    private String userIdx;

    private Long createDate;

    @Column
    private String message;

    @Builder
    public ChatMessageRdb(Long chatRoomIdx, String userIdx, String message, Long createDate) {
        this.chatRoomIdx = chatRoomIdx;
        this.userIdx = userIdx;
        this.message = message;
        this.createDate = createDate;
    }
}
