package com.paceai.domain.training;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Training Session Domain Entity.
 * <p>
 * Represents a single workout session within a training plan.
 * Immutable entity following Rich Domain Model principles.
 * </p>
 */
public final class Session {

    private final UUID id;
    private final UUID planId;
    private final LocalDate scheduledDate;
    private final SessionType type;
    private final SessionStatus status;
    private final String details;
    private final Long stravaActivityId;

    private Session(Builder builder) {
        this.id = builder.id;
        this.planId = builder.planId;
        this.scheduledDate = builder.scheduledDate;
        this.type = builder.type;
        this.status = builder.status;
        this.details = builder.details;
        this.stravaActivityId = builder.stravaActivityId;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public UUID getPlanId() {
        return planId;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public SessionType getType() {
        return type;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public String getDetails() {
        return details;
    }

    public Long getStravaActivityId() {
        return stravaActivityId;
    }

    // Domain Behavior
    public Session markAsCompleted(Long stravaActivityId) {
        // TODO: Validate transition and return new immutable instance
        return Session.builder()
                .id(this.id)
                .planId(this.planId)
                .scheduledDate(this.scheduledDate)
                .type(this.type)
                .status(SessionStatus.COMPLETED)
                .details(this.details)
                .stravaActivityId(stravaActivityId)
                .build();
    }

    public static final class Builder {
        private UUID id;
        private UUID planId;
        private LocalDate scheduledDate;
        private SessionType type;
        private SessionStatus status = SessionStatus.PENDING;
        private String details;
        private Long stravaActivityId;

        private Builder() {}

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder planId(UUID planId) {
            this.planId = planId;
            return this;
        }

        public Builder scheduledDate(LocalDate scheduledDate) {
            this.scheduledDate = scheduledDate;
            return this;
        }

        public Builder type(SessionType type) {
            this.type = type;
            return this;
        }

        public Builder status(SessionStatus status) {
            this.status = status;
            return this;
        }

        public Builder details(String details) {
            this.details = details;
            return this;
        }

        public Builder stravaActivityId(Long stravaActivityId) {
            this.stravaActivityId = stravaActivityId;
            return this;
        }

        public Session build() {
            return new Session(this);
        }
    }
}
