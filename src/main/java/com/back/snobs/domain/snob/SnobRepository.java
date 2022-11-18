package com.back.snobs.domain.snob;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SnobRepository extends JpaRepository<Snob, Long> {
    Boolean existsByCellPhoneCode(String cellPhoneCode);

    Boolean existsByUserEmail(String userEmail);
    Optional<Snob> findBySnobIdx(String snobIdx);

    Optional<Snob> findByUserEmail(String userEmail);
}