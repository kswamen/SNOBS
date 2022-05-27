package com.back.snobs.domain.reaction;

import com.back.snobs.domain.log.Log;
import com.back.snobs.domain.snob.Snob;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReactionDto {
    private Long reactionIdx;
    private Long logIdx;
    private String message;
    private Log log;
    private Snob senderSnob;
    private Snob receiverSnob;

    public Reaction toEntity() {
        return Reaction.builder()
                .log(log)
                .senderSnob(senderSnob)
                .receiverSnob(receiverSnob)
                .message(message)
                .build();
    }
}
