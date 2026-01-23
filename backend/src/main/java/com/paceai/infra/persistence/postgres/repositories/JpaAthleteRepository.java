package com.paceai.infra.persistence.postgres.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paceai.infra.persistence.postgres.entity.AthleteEntity;

@Repository
public interface JpaAthleteRepository extends JpaRepository<AthleteEntity, java.util.UUID> {

    Optional<AthleteEntity> findByStravaId(Long stravaId);
    Optional<AthleteEntity> findByEmail(String email);

    boolean existsByStravaId(Long stravaId);
    boolean existsByEmail(String email);
}
