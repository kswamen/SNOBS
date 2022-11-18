package com.back.snobs.domain.reaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReactionId implements Serializable {
    private Long log;
    private Long snob;
}
