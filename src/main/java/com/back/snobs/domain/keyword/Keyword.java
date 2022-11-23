package com.back.snobs.domain.keyword;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import reactor.util.annotation.Nullable;

import javax.persistence.*;

/*
    책의 키워드 ex) 단편소설, 자기개발서 등등..
 */
@Getter
@NoArgsConstructor
@Entity
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keywordIdx;

    @Column(nullable = false)
    private String keywordName;

    @Builder
    public Keyword(String keywordName) {
        this.keywordName = keywordName;
    }
}
