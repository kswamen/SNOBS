package com.back.snobs.dto.reaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReactionId implements Serializable {
    private Long log;
    private String snob;
}
