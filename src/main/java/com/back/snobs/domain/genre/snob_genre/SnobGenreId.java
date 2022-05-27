package com.back.snobs.domain.genre.snob_genre;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SnobGenreId implements Serializable {
    private String genre;
    private String snob;
}

