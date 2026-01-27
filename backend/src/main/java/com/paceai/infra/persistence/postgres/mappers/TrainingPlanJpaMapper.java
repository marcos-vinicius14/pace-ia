package com.paceai.infra.persistence.postgres.mappers;

import org.springframework.stereotype.Component;

import com.paceai.core.domain.athlete.AthleteId;
import com.paceai.core.domain.shared.Distance;
import com.paceai.core.domain.training.TrainingPlanId;
import com.paceai.core.domain.training.plan.GoalDistance;
import com.paceai.core.domain.training.plan.PlanStatus;
import com.paceai.core.domain.training.plan.TrainingPlan;
import com.paceai.core.domain.training.session.Sessions;
import com.paceai.infra.persistence.postgres.entity.TrainingPlanEntity;

import java.math.BigDecimal;

@Component
public class TrainingPlanJpaMapper {

    public TrainingPlanEntity toJpaEntity(TrainingPlan plan) {
        if (plan == null) {
            return null;
        }

        TrainingPlanEntity entity = new TrainingPlanEntity();
        entity.setId(plan.id().value());
        entity.setAthleteId(plan.athleteId().value());
        entity.setGoalDistance(mapGoalDistance(plan.goalDistance()));
        entity.setRaceDate(plan.raceDate());
        entity.setStatus(mapPlanStatus(plan.status()));
        entity.setAiModelVersion(plan.aiModelVersion());
        entity.setWeeklyVolumeKm(BigDecimal.valueOf(plan.weeklyVolume().getValueInKilometers()));
        entity.setStartDate(plan.startDate());

        return entity;
    }

    public TrainingPlan toDomainEntity(TrainingPlanEntity entity, Sessions sessions) {
        if (entity == null) {
            return null;
        }

        return TrainingPlan.reconstitute(
                TrainingPlanId.of(entity.getId()),
                AthleteId.of(entity.getAthleteId()),
                Distance.ofKilometers(entity.getWeeklyVolumeKm().doubleValue()),
                entity.getStartDate(),
                mapGoalDistanceJpa(entity.getGoalDistance()),
                entity.getRaceDate(),
                mapPlanStatusJpa(entity.getStatus()),
                sessions,
                entity.getAiModelVersion()
        );
    }

    private TrainingPlanEntity.GoalDistanceJpa mapGoalDistance(GoalDistance goalDistance) {
        return switch (goalDistance) {
            case FIVE_K -> TrainingPlanEntity.GoalDistanceJpa.FIVE_K;
            case TEN_K -> TrainingPlanEntity.GoalDistanceJpa.TEN_K;
            case HALF_MARATHON -> TrainingPlanEntity.GoalDistanceJpa.HALF_MARATHON;
            case MARATHON -> TrainingPlanEntity.GoalDistanceJpa.MARATHON;
        };
    }

    private GoalDistance mapGoalDistanceJpa(TrainingPlanEntity.GoalDistanceJpa goalDistanceJpa) {
        return switch (goalDistanceJpa) {
            case FIVE_K -> GoalDistance.FIVE_K;
            case TEN_K -> GoalDistance.TEN_K;
            case HALF_MARATHON -> GoalDistance.HALF_MARATHON;
            case MARATHON -> GoalDistance.MARATHON;
        };
    }

    private TrainingPlanEntity.PlanStatusJpa mapPlanStatus(PlanStatus status) {
        return switch (status) {
            case DRAFT -> TrainingPlanEntity.PlanStatusJpa.DRAFT;
            case ACTIVE -> TrainingPlanEntity.PlanStatusJpa.ACTIVE;
            case COMPLETED -> TrainingPlanEntity.PlanStatusJpa.COMPLETED;
            case CANCELLED -> TrainingPlanEntity.PlanStatusJpa.CANCELLED;
        };
    }

    private PlanStatus mapPlanStatusJpa(TrainingPlanEntity.PlanStatusJpa statusJpa) {
        return switch (statusJpa) {
            case DRAFT -> PlanStatus.DRAFT;
            case ACTIVE -> PlanStatus.ACTIVE;
            case COMPLETED -> PlanStatus.COMPLETED;
            case CANCELLED -> PlanStatus.CANCELLED;
        };
    }
}
