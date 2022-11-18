package com.back.snobs.domain.log;

import com.back.snobs.domain.BaseTimeEntity;
import com.back.snobs.domain.book.Book;
import com.back.snobs.domain.snob.Snob;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@RequiredArgsConstructor
@Entity
@DynamicInsert
public class Log extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logIdx;

    @Column(nullable = false)
    private String logTitle;

    // 독후감 본문
    @Column(nullable = false, columnDefinition = "varchar(1000)")
    private String previewText;

    // default tinyInt(1) on MySQL
    @Column(columnDefinition = "boolean default true")
    private Boolean isPublic;

    @Column(columnDefinition = "varchar(500)")
    private String logQuestion;

    @ManyToOne
    @JoinColumn(name = "bookId")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "snobIdx")
    private Snob snob;

    @Builder
    public Log(String logTitle, String previewText, Boolean isPublic, String logQuestion, Book book, Snob snob) {
        this.logTitle = logTitle;
        this.previewText = previewText;
        this.isPublic = isPublic;
        this.logQuestion = logQuestion;
        this.book = book;
        this.snob = snob;
    }

    public void LogUpdate(String logTitle, String previewText, Boolean isPublic, String logQuestion) {
        this.logTitle = logTitle;
        this.previewText = previewText;
        this.isPublic = isPublic;
        this.logQuestion = logQuestion;
    }
}
