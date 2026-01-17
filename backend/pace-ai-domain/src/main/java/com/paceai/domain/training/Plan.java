package com.paceai.domain.training;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Training Plan Domain Entity.
 * <p>
 * A Rich Domain Entity that encapsulates the business rules for a training plan.
 * This entity is immutable and contains behavior, not just data.
 * </p>
 */
public final class Plan {

    private final UUID id;
    private final UUID athleteId;
    private final GoalDistance goalDistance;
    private final LocalDate raceDate;
    private final PlanStatus status;
    private final List<Session> sessions;
    private final String aiModelVersion;

    private Plan(Builder builder) {
        this.id = builder.id;
        this.athleteId = builder.athleteId;
        this.goalDistance = builder.goalDistance;
        this.raceDate = builder.raceDate;
        this.status = builder.status;
        this.sessions = builder.sessions == null ? List.of() : List.copyOf(builder.sessions);
        this.aiModelVersion = builder.aiModelVersion;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters (no setters - immutable)
    public UUID getId() {
        return id;
    }

    public UUID getAthleteId() {
        return athleteId;
    }

    public GoalDistance getGoalDistance() {
        return goalDistance;
    }

    public LocalDate getRaceDate() {
        return raceDate;
    }

    public PlanStatus getStatus() {
        return status;
    }

    public List<Session> getSessions() {
        return Collections.unmodifiableList(sessions);
    }

    public String getAiModelVersion() {
        return aiModelVersion;
    }

    // Domain Behavior
    public double calculateWeeklyLoad(int weekNumber) {
        // TODO: Implement weekly load calculation based on sessions
        return 0.0;
    }

    public boolean isProgressionSafe(int weekNumber) {
        // TODO: Implement 10-15% progression rule validation
        // If week N > week N-1 * 1.15, return false
        return true;
    }

    public Plan activate() {
        // TODO: Transition to ACTIVE status using state machine
        return Plan.builder()
                .id(this.id)
                .athleteId(this.athleteId)
                .goalDistance(this.goalDistance)
                .raceDate(this.raceDate)
                .status(PlanStatus.ACTIVE)
                .sessions(this.sessions)
                .aiModelVersion(this.aiModelVersion)
                .build();
    }

    public static final class Builder {
        private UUID id;
        private UUID athleteId;
        private GoalDistance goalDistance;
        private LocalDate raceDate;
        private PlanStatus status = PlanStatus.DRAFT;
        private List<Session> sessions;
        private String aiModelVersion;

        private Builder() {}

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder athleteId(UUID athleteId) {
            this.athleteId = athleteId;
            return this;
        }

        public Builder goalDistance(GoalDistance goalDistance) {
            this.goalDistance = goalDistance;
            return this;
        }

        public Builder raceDate(LocalDate raceDate) {
            this.raceDate = raceDate;
            return this;
        }

        public Builder status(PlanStatus status) {
            this.status = status;
            return this;
        }

        public Builder sessions(List<Session> sessions) {
            this.sessions = sessions;
            return this;
        }

        public Builder aiModelVersion(String aiModelVersion) {
            this.aiModelVersion = aiModelVersion;
            return this;
        }

        public Plan build() {
            return new Plan(this);
        }
    }
}
