package com.paceai.infrastructure.persistence.postgres;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * JPA Entity for Training Plan.
 * <p>
 * This is an Infrastructure concern - a JPA-annotated entity that maps
 * to the database table. It is separate from the Domain Entity.
 * </p>
 */
@Entity
@Table(name = "training_plans")
public class PlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "athlete_id", nullable = false)
    private UUID athleteId;

    @Enumerated(EnumType.STRING)
    @Column(name = "goal_distance", nullable = false)
    private GoalDistanceJpa goalDistance;

    @Column(name = "race_date")
    private LocalDate raceDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PlanStatusJpa status;

    @Column(name = "ai_model_version")
    private String aiModelVersion;

    // JPA requires default constructor
    protected PlanEntity() {}

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(UUID athleteId) {
        this.athleteId = athleteId;
    }

    public GoalDistanceJpa getGoalDistance() {
        return goalDistance;
    }

    public void setGoalDistance(GoalDistanceJpa goalDistance) {
        this.goalDistance = goalDistance;
    }

    public LocalDate getRaceDate() {
        return raceDate;
    }

    public void setRaceDate(LocalDate raceDate) {
        this.raceDate = raceDate;
    }

    public PlanStatusJpa getStatus() {
        return status;
    }

    public void setStatus(PlanStatusJpa status) {
        this.status = status;
    }

    public String getAiModelVersion() {
        return aiModelVersion;
    }

    public void setAiModelVersion(String aiModelVersion) {
        this.aiModelVersion = aiModelVersion;
    }

    // JPA Enums (Infrastructure-specific)
    public enum GoalDistanceJpa {
        FIVE_K, TEN_K, HALF_MARATHON, MARATHON
    }

    public enum PlanStatusJpa {
        DRAFT, ACTIVE, COMPLETED, CANCELLED
    }
}
