package com.paceai.infrastructure.persistence.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA Repository for PlanEntity.
 * <p>
 * This is the actual JPA repository interface used by PostgresPlanRepository.
 * It extends JpaRepository to get CRUD operations for free.
 * </p>
 */
@Repository
public interface JpaPlanRepository extends JpaRepository<PlanEntity, UUID> {

    /**
     * Finds all plans for a given athlete.
     *
     * @param athleteId the athlete ID
     * @return list of plan entities
     */
    List<PlanEntity> findByAthleteId(UUID athleteId);
}
