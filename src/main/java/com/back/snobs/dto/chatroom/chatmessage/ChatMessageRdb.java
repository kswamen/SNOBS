package com.back.snobs.dto.chatroom.chatmessage;

import com.back.snobs.dto.BaseTimeEntity;
import com.back.snobs.dto.chatroom.ChatRoom;
import com.back.snobs.dto.snob.Snob;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDateTime;

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
