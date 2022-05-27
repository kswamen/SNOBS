package com.back.snobs.domain.snob;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SnobRepository extends JpaRepository<Snob, String> {
    Boolean existsByCellPhoneCode(String cellPhoneCode);
    Optional<Snob> findBySnobIdx(String snobIdx);
}