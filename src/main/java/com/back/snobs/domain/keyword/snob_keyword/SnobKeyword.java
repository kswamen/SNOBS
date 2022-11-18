package com.back.snobs.domain.keyword.snob_keyword;

import com.back.snobs.domain.keyword.Keyword;
import com.back.snobs.domain.snob.Snob;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@RequiredArgsConstructor
@Entity
@DynamicInsert
@IdClass(SnobKeywordId.class)
public class SnobKeyword {
    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    @JoinColumn(name = "keywordIdx")
    private Keyword keyword;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    @JoinColumn(name = "snobIdx")
    private Snob snob;

    @Column(columnDefinition = "tinyint(1) default 1")
    private Boolean preference;

    @Builder
    public SnobKeyword(Keyword keyword, Snob snob, Boolean preference) {
        this.keyword = keyword;
        this.snob = snob;
        this.preference = preference;
    }

    public void setPreference(Boolean preference) {
        this.preference = preference;
    }
}
