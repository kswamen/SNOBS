package com.back.snobs.dto.keyword.snob_keyword;

import com.back.snobs.dto.keyword.Keyword;
import com.back.snobs.dto.snob.Snob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SnobKeywordRepository extends JpaRepository<SnobKeyword, SnobKeywordId> {
    @Query(value = "select sk from SnobKeyword sk where sk.snob.userEmail = :userEmail and sk.preference = true")
    List<SnobKeyword> findBySnob_UserEmail(@Param("userEmail") String userEmail);
}