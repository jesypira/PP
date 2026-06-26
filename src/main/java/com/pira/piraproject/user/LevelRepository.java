package com.pira.piraproject.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LevelRepository extends JpaRepository<Level, Long> {

    Optional<Level> findFirstByIdGreaterThanOrderByIdAsc(Long levelId);
}
