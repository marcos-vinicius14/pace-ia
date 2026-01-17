package com.paceai.infrastructure.persistence.postgres;

import com.paceai.infrastructure.persistence.postgres.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaPlanRepository extends JpaRepository<PlanEntity, UUID> {

    List<PlanEntity> findByAthleteId(UUID athleteId);

    @Query("SELECT p FROM PlanEntity p LEFT JOIN FETCH p.sessions s WHERE p.id = :id")
    Optional<PlanEntity> findByIdWithSessions(@org.springframework.data.repository.query.Param("id") UUID id);

    @Query("SELECT p FROM PlanEntity p LEFT JOIN FETCH p.sessions s WHERE p.athleteId = :athleteId")
    List<PlanEntity> findByAthleteIdWithSessions(@org.springframework.data.repository.query.Param("athleteId") UUID athleteId);
}
