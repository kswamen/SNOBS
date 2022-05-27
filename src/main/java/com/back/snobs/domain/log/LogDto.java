package com.back.snobs.domain.log;

import com.back.snobs.domain.book.Book;
import com.back.snobs.domain.snob.Snob;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LogDto {
    private Long logIdx;
    private String logTitle;
    private String previewText;
    private Boolean isPublic;
    private String logQuestion;
    private Book book;
    private String bookId;
    private Snob snob;

    public Log toEntity() {
        return Log.builder()
                .logTitle(logTitle)
                .previewText(previewText)
                .isPublic(isPublic)
                .logQuestion(logQuestion)
                .book(book)
                .snob(snob)
                .build();
    }
}
