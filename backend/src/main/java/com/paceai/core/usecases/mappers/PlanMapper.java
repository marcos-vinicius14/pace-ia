package com.paceai.core.usecases.mappers;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.paceai.core.domain.athlete.AthleteId;
import com.paceai.core.domain.shared.Distance;
import com.paceai.core.domain.shared.Result;
import com.paceai.core.domain.training.plan.GoalDistance;
import com.paceai.core.domain.training.plan.TrainingPlan;
import com.paceai.core.domain.training.session.Session;
import com.paceai.core.usecases.dtos.CreatePlanRequest;
import com.paceai.core.usecases.dtos.PlanDto;
import com.paceai.core.usecases.dtos.PlanResponse;
import com.paceai.core.usecases.dtos.SessionDto;

/**
 * Mapper between Domain Entities and DTOs.
 */
public class PlanMapper {

    public Result<TrainingPlan> toDomain(CreatePlanRequest request) {
        if (request == null) {
            return Result.failure("Request não pode ser nula");
        }

        Result<AthleteId> athleteIdResult = parseAthleteId(request.athleteId());
        if (athleteIdResult.isFailure()) {
            return Result.failure(athleteIdResult.getErrorMessage());
        }

        Result<GoalDistance> goalDistanceResult = parseGoalDistance(request.goalDistance());
        if (goalDistanceResult.isFailure()) {
            return Result.failure(goalDistanceResult.getErrorMessage());
        }

        Result<Distance> weeklyVolumeResult = Distance.createKilometers(request.weeklyVolumeKm());
        if (weeklyVolumeResult.isFailure()) {
            return Result.failure(weeklyVolumeResult.getErrorMessage());
        }

        if (request.startDate() == null) {
            return Result.failure("Data de início é obrigatória");
        }

        if (request.raceDate() == null) {
            return Result.failure("Data da prova é obrigatória");
        }

        if (request.startDate().isAfter(request.raceDate())) {
            return Result.failure("Data de início deve ser anterior à data da prova");
        }

        TrainingPlan plan = TrainingPlan.create(
                athleteIdResult.getValue(),
                weeklyVolumeResult.getValue(),
                request.startDate(),
                goalDistanceResult.getValue(),
                request.raceDate()
        );

        return Result.success(plan);
    }

    public PlanResponse toResponse(TrainingPlan plan) {
        if (plan == null) {
            return null;
        }

        return new PlanResponse(
                plan.id().value().toString(),
                plan.athleteId().value().toString(),
                plan.goalDistance().getDisplayName(),
                plan.startDate(),
                plan.raceDate(),
                plan.status().name(),
                plan.weeklyVolume().getValueInKilometers()
        );
    }

    public PlanDto toDto(TrainingPlan plan) {
        if (plan == null) {
            return null;
        }

        return new PlanDto(
                plan.id().value().toString(),
                plan.athleteId().value().toString(),
                plan.goalDistance().getDisplayName(),
                plan.raceDate(),
                plan.status().name(),
                toSessionDtos(plan.sessions().asList())
        );
    }

    public List<SessionDto> toSessionDtos(List<Session> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return List.of();
        }

        return sessions.stream()
                .map(this::toDto)
                .toList();
    }

    public SessionDto toDto(Session session) {
        if (session == null) {
            return null;
        }

        return new SessionDto(
                session.id() != null ? session.id().value().toString() : null,
                session.scheduledDate(),
                session.type().name(),
                session.status().name(),
                session.details()
        );
    }

    private Result<AthleteId> parseAthleteId(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return Result.failure("AthleteId é obrigatório");
        }

        try {
            return Result.success(AthleteId.of(UUID.fromString(rawValue.trim())));
        } catch (IllegalArgumentException ex) {
            return Result.failure("AthleteId inválido: " + rawValue);
        }
    }

    private Result<GoalDistance> parseGoalDistance(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return Result.failure("Distância alvo é obrigatória");
        }

        String normalized = rawValue.trim()
                .toUpperCase(Locale.ROOT)
                .replace(" ", "_")
                .replace("-", "_");

        return switch (normalized) {
            case "5K", "FIVE_K" -> Result.success(GoalDistance.FIVE_K);
            case "10K", "TEN_K" -> Result.success(GoalDistance.TEN_K);
            case "HM", "HALF_MARATHON" -> Result.success(GoalDistance.HALF_MARATHON);
            case "MARATHON" -> Result.success(GoalDistance.MARATHON);
            default -> Result.failure("Distância alvo inválida: " + rawValue);
        };
    }
}
