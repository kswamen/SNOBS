package com.back.snobs.domain.book;

import com.back.snobs.domain.BaseTimeEntity;
import com.back.snobs.domain.book.snob_book.SnobBook;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlID;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UK_booKId", columnNames = {"bookId"}),
})
public class Book extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookIdx;

    @Column(nullable = false)
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
