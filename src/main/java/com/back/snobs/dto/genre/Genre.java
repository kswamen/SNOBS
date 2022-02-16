package com.back.snobs.dto.genre;

import com.back.snobs.dto.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Getter
@RequiredArgsConstructor
@Entity
public class Genre {
    @Id
    private String genreName;

    @Builder
    public Genre(String genreName) {
        this.genreName = genreName;
    }
}
