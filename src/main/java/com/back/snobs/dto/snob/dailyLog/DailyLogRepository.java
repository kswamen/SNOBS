package com.back.snobs.dto.snob.dailyLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DailyLogRepository extends JpaRepository<DailyLog, DailyLogId> {
    Long countBySnob_userEmail(String userEmail);
    @Query(value = "select distinct dl from DailyLog dl join fetch dl.log where dl.snob.userEmail = :userEmail")
    List<DailyLog> findAllBySnob_userEmail(@Param("userEmail") String userEmail);
}