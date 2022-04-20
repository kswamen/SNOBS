package com.back.snobs.dto.book.snob_book;

import com.back.snobs.dto.BaseTimeEntity;
import com.back.snobs.dto.book.Book;
import com.back.snobs.dto.snob.Snob;
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
@IdClass(SnobBookId.class)
public class SnobBook extends BaseTimeEntity {
    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "bookId")
    private Book book;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "userEmail")
    private Snob snob;

    @Column(columnDefinition = "boolean default false")
    private Boolean readed;

    @Builder
    public SnobBook(Book book, Snob snob, Boolean readed) {
        this.book = book;
        this.snob = snob;
        this.readed = readed;
    }

    public void updateReaded(Boolean readed) {
        this.readed = readed;
    }
}
