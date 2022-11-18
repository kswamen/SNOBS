package com.back.snobs.domain.genre;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Getter
@RequiredArgsConstructor
@Entity
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long genreIdx;

    @Column(nullable = false)
    private String genreName;

    @Builder
    public Genre(String genreName) {
        this.genreName = genreName;
    }
}
