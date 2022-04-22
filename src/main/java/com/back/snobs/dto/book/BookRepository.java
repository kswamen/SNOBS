package com.back.snobs.dto.book;

import com.back.snobs.dto.book.snob_book.SnobBook;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, String> {
    // @ManyToOne에서는 fetch join, 일반 join 둘 다 페이징 가능, 오류도 없음
    @Query(value = "select b from Book b join b.snobBooks sb where sb.snob.userEmail = :userEmail and sb.readed = :readed")
    List<Book> findByBookWithFetchJoin(@Param("userEmail") String userEmail, @Param("readed") Boolean readed, Pageable pageable);

    @Query(value = "select b from Book b join b.snobBooks sb")
    List<Book> findByBookWithFetchJoin2(Pageable pageable);
}