package com.back.snobs.dto.genre.snob_genre;

import com.back.snobs.dto.genre.Genre;
import com.back.snobs.dto.snob.Snob;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@RequiredArgsConstructor
@Entity
@DynamicInsert
@IdClass(SnobGenreId.class)
public class SnobGenre {
    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    @JoinColumn(name = "genreName")
    private Genre genre;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    @JoinColumn(name = "userEmail")
    private Snob snob;

    @Column(columnDefinition = "tinyint(1) default 1")
    private Boolean preference;

    @Builder
    public SnobGenre(Genre genre, Snob snob, Boolean preference) {
        this.genre = genre;
        this.snob = snob;
        this.preference = preference;
    }

    public void setPreference(Boolean preference) {
        this.preference = preference;
    }
}
