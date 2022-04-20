package com.back.snobs.dto.book;

import com.back.snobs.dto.BaseTimeEntity;
import com.back.snobs.dto.book.snob_book.SnobBook;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Entity
public class Book extends BaseTimeEntity {
    @Id
    private String bookId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column
    private String translator;

    @Column
    private String thumbnailImageUrl;

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<SnobBook> snobBooks;

    @Builder
    public Book(String bookId, String title, String author, String translator, String thumbnailImageUrl) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.translator = translator;
        this.thumbnailImageUrl = thumbnailImageUrl;
    }
}
