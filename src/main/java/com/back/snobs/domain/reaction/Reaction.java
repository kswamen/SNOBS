package com.back.snobs.domain.reaction;

import com.back.snobs.domain.BaseTimeEntity;
import com.back.snobs.domain.log.Log;
import com.back.snobs.domain.snob.Snob;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Getter
@RequiredArgsConstructor
@Entity
public class Reaction extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reactionIdx;

    @ManyToOne
    @JoinColumn(name = "logIdx", nullable = false)
    private Log log;

    @ManyToOne
    @JoinColumn(name = "senderEmail", nullable = false)
    private Snob senderSnob;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "receiverEmail", nullable = false)
    private Snob receiverSnob;

    @Column(nullable = false)
    private String message;

    @Builder
    public Reaction(Log log, Snob senderSnob, Snob receiverSnob, String message) {
        this.log = log;
        this.senderSnob = senderSnob;
        this.receiverSnob = receiverSnob;
        this.message = message;
    }

    public void updateMessage(String message) {
        this.message = message;
    }
}
