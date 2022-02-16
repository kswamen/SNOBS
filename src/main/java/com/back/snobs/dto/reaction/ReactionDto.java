package com.back.snobs.dto.reaction;

import com.back.snobs.dto.log.Log;
import com.back.snobs.dto.snob.Snob;
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
