package com.back.snobs.dto.genre.snob_genre;

import com.back.snobs.dto.genre.Genre;
import com.back.snobs.dto.snob.Snob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SnobGenreRepository extends JpaRepository<SnobGenre, SnobGenreId> {
    @Query(value = "select sg from SnobGenre sg where sg.snob.userEmail = :userEmail and sg.preference = true")
    List<SnobGenre> findBySnob_UserEmail(@Param("userEmail") String userEmail);
}