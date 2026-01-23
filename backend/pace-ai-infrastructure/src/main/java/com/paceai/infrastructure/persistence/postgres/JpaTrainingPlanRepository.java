package com.paceai.infrastructure.persistence.postgres;

import com.paceai.infrastructure.persistence.postgres.entity.TrainingPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaTrainingPlanRepository extends JpaRepository<TrainingPlanEntity, UUID> {

    List<TrainingPlanEntity> findByAthleteId(UUID athleteId);

    @Query("SELECT p FROM TrainingPlanEntity p LEFT JOIN FETCH p.sessions s WHERE p.id = :id")
    Optional<TrainingPlanEntity> findByIdWithSessions(@org.springframework.data.repository.query.Param("id") UUID id);

    @Query("SELECT p FROM TrainingPlanEntity p LEFT JOIN FETCH p.sessions s WHERE p.athleteId = :athleteId")
    List<TrainingPlanEntity> findByAthleteIdWithSessions(@org.springframework.data.repository.query.Param("athleteId") UUID athleteId);
}
