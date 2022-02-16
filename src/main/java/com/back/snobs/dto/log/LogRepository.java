package com.back.snobs.dto.log;

import com.back.snobs.dto.snob.Snob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findBySnob_UserEmail(@Param("writerEmail") String userEmail);
}