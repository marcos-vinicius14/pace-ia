package com.paceai.domain.training.plan;

import com.paceai.domain.athlete.AthleteId;
import com.paceai.domain.shared.Result;
import com.paceai.domain.shared.Distance;
import com.paceai.domain.training.TrainingPlanId;
import com.paceai.domain.training.session.Sessions;
import java.time.LocalDate;
import java.util.Objects;

public final class TrainingPlan {

    private final TrainingPlanId id;
    private final AthleteId athleteId;
    private final Distance weeklyVolume;
    private final LocalDate startDate;
    private final GoalDistance goalDistance;
    private final LocalDate raceDate;
    private final PlanStatus status;
    private final Sessions sessions;
    private final String aiModelVersion;

    private TrainingPlan(
            TrainingPlanId id,
            AthleteId athleteId,
            Distance weeklyVolume,
            LocalDate startDate,
            GoalDistance goalDistance,
            LocalDate raceDate,
            PlanStatus status,
            Sessions sessions,
            String aiModelVersion
    ) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.athleteId = Objects.requireNonNull(athleteId, "athleteId cannot be null");
        this.weeklyVolume = Objects.requireNonNull(weeklyVolume, "weeklyVolume cannot be null");
        this.startDate = Objects.requireNonNull(startDate, "startDate cannot be null");
        this.goalDistance = Objects.requireNonNull(goalDistance, "goalDistance cannot be null");
        this.raceDate = Objects.requireNonNull(raceDate, "raceDate cannot be null");
        this.status = Objects.requireNonNull(status, "status cannot be null");
        this.sessions = Objects.requireNonNull(sessions, "sessions cannot be null");
        this.aiModelVersion = aiModelVersion;
    }

    public static TrainingPlan create(
            AthleteId athleteId,
            Distance weeklyVolume,
            LocalDate startDate,
            GoalDistance goalDistance,
            LocalDate raceDate
    ) {
        return new TrainingPlan(
                TrainingPlanId.create(),
                athleteId,
                weeklyVolume,
                startDate,
                goalDistance,
                raceDate,
                PlanStatus.DRAFT,
                Sessions.empty(),
                "v1" // Default for now
        );
    }

    public static Result<TrainingPlan> createWithValidation(
            AthleteId athleteId,
            Distance weeklyVolume,
            LocalDate startDate,
            GoalDistance goalDistance,
            LocalDate raceDate,
            TrainingPlan previousPlan
    ) {
        Result<Void> validationResult = validateProgression(weeklyVolume, previousPlan.weeklyVolume());
        
        if (validationResult.isFailure()) {
            return Result.failure(validationResult.getErrorMessage());
        }

        return Result.success(new TrainingPlan(
                TrainingPlanId.create(),
                athleteId,
                weeklyVolume,
                startDate,
                goalDistance,
                raceDate,
                PlanStatus.DRAFT,
                Sessions.empty(),
                "v1"
        ));
    }

    private static Result<Void> validateProgression(Distance current, Distance previous) {
        double previousKm = previous.getValueInKilometers();
        if (previousKm <= 0) return Result.success(null);

        double currentKm = current.getValueInKilometers();
        double increase = ((currentKm - previousKm) / previousKm) * 100;

        if (increase > 15.0) {
             return Result.failure(
                    String.format("Excessive load: %.2f%% increase exceeds 15%% limit. Previous: %.2f km, New: %.2f km",
                            increase, previousKm, currentKm)
            );
        }
        return Result.success(null);
    }

    public static TrainingPlan reconstitute(
            TrainingPlanId id,
            AthleteId athleteId,
            Distance weeklyVolume,
            LocalDate startDate,
            GoalDistance goalDistance,
            LocalDate raceDate,
            PlanStatus status,
            Sessions sessions,
            String aiModelVersion
    ) {
        return new TrainingPlan(
                id,
                athleteId,
                weeklyVolume,
                startDate,
                goalDistance,
                raceDate,
                status,
                sessions,
                aiModelVersion
        );
    }

    public TrainingPlanId id() { return id; }
    public AthleteId athleteId() { return athleteId; }
    public Distance weeklyVolume() { return weeklyVolume; }
    public LocalDate startDate() { return startDate; }
    public GoalDistance goalDistance() { return goalDistance; }
    public LocalDate raceDate() { return raceDate; }
    public PlanStatus status() { return status; }
    public Sessions sessions() { return sessions; }
    public String aiModelVersion() { return aiModelVersion; }
    
    // Behavior
    public TrainingPlan activate() {
        return new TrainingPlan(
            this.id,
            this.athleteId,
            this.weeklyVolume,
            this.startDate,
            this.goalDistance,
            this.raceDate,
            PlanStatus.ACTIVE,
            this.sessions,
            this.aiModelVersion
        );
    }
}