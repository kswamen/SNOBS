package com.back.snobs.domain.book;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookDto {
    private String bookId;
    private String title;
    private String author;
    private String translator;
    private String thumbnailImageUrl;

    public BookDto(Book entity) {
        this.bookId = entity.getBookId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.translator = entity.getTranslator();
        this.thumbnailImageUrl = entity.getThumbnailImageUrl();
    }

    public Book toEntity() {
        return Book.builder()
                .bookId(bookId)
                .title(title)
                .author(author)
                .translator(translator)
                .thumbnailImageUrl(thumbnailImageUrl)
                .build();
    }
}
