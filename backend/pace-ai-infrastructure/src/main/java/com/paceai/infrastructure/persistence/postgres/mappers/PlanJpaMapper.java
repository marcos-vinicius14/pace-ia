package com.paceai.infrastructure.persistence.postgres.mappers;

import com.paceai.domain.training.*;
import com.paceai.infrastructure.persistence.postgres.PlanEntity;
import com.paceai.infrastructure.persistence.postgres.entity.SessionEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlanJpaMapper {

    public PlanEntity toJpaEntity(Plan plan) {
        if (plan == null) {
            return null;
        }

        PlanEntity entity = new PlanEntity();
        entity.setId(plan.getId());
        entity.setAthleteId(plan.getAthleteId());
        entity.setGoalDistance(mapGoalDistance(plan.getGoalDistance()));
        entity.setStatus(mapPlanStatus(plan.getStatus()));
        entity.setAiModelVersion(plan.getAiModelVersion());

        return entity;
    }

    public Plan toDomainEntity(PlanEntity entity, List<Session> sessions) {
        if (entity == null) {
            return null;
        }

        return Plan.builder()
                .id(entity.getId())
                .athleteId(entity.getAthleteId())
                .goalDistance(mapGoalDistanceJpa(entity.getGoalDistance()))
                .status(mapPlanStatusJpa(entity.getStatus()))
                .sessions(sessions)
                .aiModelVersion(entity.getAiModelVersion())
                .build();
    }

    private PlanEntity.GoalDistanceJpa mapGoalDistance(GoalDistance goalDistance) {
        return switch (goalDistance) {
            case FIVE_K -> PlanEntity.GoalDistanceJpa.FIVE_K;
            case TEN_K -> PlanEntity.GoalDistanceJpa.TEN_K;
            case HALF_MARATHON -> PlanEntity.GoalDistanceJpa.HALF_MARATHON;
            case MARATHON -> PlanEntity.GoalDistanceJpa.MARATHON;
        };
    }

    private GoalDistance mapGoalDistanceJpa(PlanEntity.GoalDistanceJpa goalDistanceJpa) {
        return switch (goalDistanceJpa) {
            case FIVE_K -> GoalDistance.FIVE_K;
            case TEN_K -> GoalDistance.TEN_K;
            case HALF_MARATHON -> GoalDistance.HALF_MARATHON;
            case MARATHON -> GoalDistance.MARATHON;
        };
    }

    private PlanEntity.PlanStatusJpa mapPlanStatus(PlanStatus status) {
        return switch (status) {
            case DRAFT -> PlanEntity.PlanStatusJpa.DRAFT;
            case ACTIVE -> PlanEntity.PlanStatusJpa.ACTIVE;
            case COMPLETED -> PlanEntity.PlanStatusJpa.COMPLETED;
            case CANCELLED -> PlanEntity.PlanStatusJpa.CANCELLED;
        };
    }

    private PlanStatus mapPlanStatusJpa(PlanEntity.PlanStatusJpa statusJpa) {
        return switch (statusJpa) {
            case DRAFT -> PlanStatus.DRAFT;
            case ACTIVE -> PlanStatus.ACTIVE;
            case COMPLETED -> PlanStatus.COMPLETED;
            case CANCELLED -> PlanStatus.CANCELLED;
        };
    }
}
