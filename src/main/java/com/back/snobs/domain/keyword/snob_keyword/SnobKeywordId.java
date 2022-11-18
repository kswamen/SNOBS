package com.back.snobs.domain.keyword.snob_keyword;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SnobKeywordId implements Serializable {
    private Long keyword;
    private Long snob;
}
