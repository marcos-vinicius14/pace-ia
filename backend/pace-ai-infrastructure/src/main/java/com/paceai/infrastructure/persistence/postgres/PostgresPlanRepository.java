package com.paceai.infrastructure.persistence.postgres;

import com.paceai.domain.ports.PlanRepository;
import com.paceai.domain.training.Plan;
import com.paceai.infrastructure.persistence.postgres.mappers.PlanJpaMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * PostgreSQL implementation of the PlanRepository Port.
 * <p>
 * This is an Adapter (in Hexagonal Architecture terms) that implements
 * the PlanRepository interface using JPA/Hibernate with PostgreSQL.
 * </p>
 */
@Repository
public class PostgresPlanRepository implements PlanRepository {

    private final JpaPlanRepository jpaPlanRepository;
    private final PlanJpaMapper planJpaMapper;

    public PostgresPlanRepository(JpaPlanRepository jpaPlanRepository, PlanJpaMapper planJpaMapper) {
        this.jpaPlanRepository = jpaPlanRepository;
        this.planJpaMapper = planJpaMapper;
    }

    @Override
    public Plan save(Plan plan) {
        // TODO: Convert domain entity to JPA entity, save, convert back
        return null;
    }

    @Override
    public Optional<Plan> findById(UUID id) {
        // TODO: Find JPA entity, convert to domain entity
        return Optional.empty();
    }

    @Override
    public List<Plan> findByAthleteId(UUID athleteId) {
        // TODO: Find JPA entities, convert to domain entities
        return List.of();
    }

    @Override
    public void deleteById(UUID id) {
        // TODO: Delete JPA entity by ID
    }

    @Override
    public boolean existsById(UUID id) {
        // TODO: Check if JPA entity exists
        return false;
    }
}
