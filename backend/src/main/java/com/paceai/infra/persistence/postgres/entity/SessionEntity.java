package com.paceai.infra.persistence.postgres.entity;

import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "training_sessions")
public class SessionEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private TrainingPlanEntity plan;

    @Column(name = "scheduled_date", nullable = false)
    private java.time.LocalDate scheduledDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SessionTypeJpa type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SessionStatusJpa status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "details_jsonb", columnDefinition = "jsonb")
    private String detailsJsonb;

    @Column(name = "strava_activity_id")
    private Long stravaActivityId;

    public SessionEntity() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TrainingPlanEntity getPlan() {
        return plan;
    }

    public void setPlan(TrainingPlanEntity plan) {
        this.plan = plan;
    }

    public java.time.LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(java.time.LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public SessionTypeJpa getType() {
        return type;
    }

    public void setType(SessionTypeJpa type) {
        this.type = type;
    }

    public SessionStatusJpa getStatus() {
        return status;
    }

    public void setStatus(SessionStatusJpa status) {
        this.status = status;
    }

    public String getDetailsJsonb() {
        return detailsJsonb;
    }

    public void setDetailsJsonb(String detailsJsonb) {
        this.detailsJsonb = detailsJsonb;
    }

    public Long getStravaActivityId() {
        return stravaActivityId;
    }

    public void setStravaActivityId(Long stravaActivityId) {
        this.stravaActivityId = stravaActivityId;
    }

    public enum SessionTypeJpa {
        REST, EASY, LONG_RUN, TEMPO, INTERVAL, FARTLEK, RECOVERY, RACE
    }

    public enum SessionStatusJpa {
        PENDING, COMPLETED, MISSED, SKIPPED
    }
}
