package com.back.snobs.domain.book.snob_book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SnobBookId implements Serializable {
    private String snob;
    private String book;
}
