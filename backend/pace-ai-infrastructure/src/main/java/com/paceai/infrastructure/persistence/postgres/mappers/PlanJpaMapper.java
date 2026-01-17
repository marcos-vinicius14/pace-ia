package com.paceai.infrastructure.persistence.postgres.mappers;

import com.paceai.domain.training.Plan;
import com.paceai.infrastructure.persistence.postgres.PlanEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper between Domain Entity and JPA Entity.
 */
@Component
public class PlanJpaMapper {

    public PlanEntity toJpaEntity(Plan plan) {
        // TODO: Implement domain to JPA conversion
        return null;
    }

    public Plan toDomainEntity(PlanEntity entity) {
        // TODO: Implement JPA to domain conversion
        return null;
    }
}
