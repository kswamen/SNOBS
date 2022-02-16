package com.back.snobs.dto.log;

import com.back.snobs.dto.book.Book;
import com.back.snobs.dto.snob.Snob;
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
