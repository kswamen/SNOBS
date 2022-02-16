package com.back.snobs.dto.reaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    @Query(value = "select r from Reaction r where r.senderSnob.userEmail = :userEmail and r.log.logIdx = :logIdx")
    Optional<Reaction> findBysenderEmailAndlogIdx(@Param("userEmail") String userEmail, @Param("logIdx") Long logIdx);
    List<Reaction> findAllByReceiverSnob_userEmail(String userEmail);
}