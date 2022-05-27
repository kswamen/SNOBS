package com.back.snobs.domain.book.snob_book;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SnobBookRepository extends JpaRepository<SnobBook, SnobBookId> {
    @Query(value = "select sb from SnobBook sb join fetch sb.book where sb.snob.userEmail = :userEmail and sb.readed = :readed")
    List<SnobBook> findBySnob_UserEmailWithFetchJoin(@Param("userEmail") String userEmail, @Param("readed") Boolean readed);

    @Query(value = "select sb from SnobBook sb join fetch sb.book where sb.snob.userEmail = :userEmail and sb.readed = :readed")
    List<SnobBook> findBySnob_UserEmail(@Param("userEmail") String userEmail, @Param("readed") Boolean readed, Pageable pageable);
}