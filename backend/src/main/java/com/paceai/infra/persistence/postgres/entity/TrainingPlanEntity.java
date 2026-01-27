package com.paceai.infra.persistence.postgres.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "training_plans")
public class TrainingPlanEntity {

    @Id
    private UUID id;

    @Column(name = "athlete_id", nullable = false)
    private UUID athleteId;

    @Enumerated(EnumType.STRING)
    @Column(name = "goal_distance", nullable = false)
    private GoalDistanceJpa goalDistance;

    @Column(name = "race_date", nullable = false)
    private LocalDate raceDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PlanStatusJpa status;

    @Column(name = "ai_model_version")
    private String aiModelVersion;

    @Column(name = "weekly_volume_km", nullable = false, precision = 10, scale = 2)
    private BigDecimal weeklyVolumeKm;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SessionEntity> sessions = new ArrayList<>();

    public TrainingPlanEntity() {}

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

    public BigDecimal getWeeklyVolumeKm() {
        return weeklyVolumeKm;
    }

    public void setWeeklyVolumeKm(BigDecimal weeklyVolumeKm) {
        this.weeklyVolumeKm = weeklyVolumeKm;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public List<SessionEntity> getSessions() {
        return sessions;
    }

    public void setSessions(List<SessionEntity> sessions) {
        this.sessions = sessions;
    }

    public enum GoalDistanceJpa {
        FIVE_K, TEN_K, HALF_MARATHON, MARATHON
    }

    public enum PlanStatusJpa {
        DRAFT, ACTIVE, COMPLETED, CANCELLED
    }
}
