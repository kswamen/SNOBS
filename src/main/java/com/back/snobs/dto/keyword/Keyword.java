package com.back.snobs.dto.keyword;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
    책의 키워드 ex) 단편소설, 자기개발서 등등..
 */
@Getter
@NoArgsConstructor
@Entity
public class Keyword {
    @Id
    private String keywordName;

    @Builder
    public Keyword(String keywordName) {
        this.keywordName = keywordName;
    }
}
