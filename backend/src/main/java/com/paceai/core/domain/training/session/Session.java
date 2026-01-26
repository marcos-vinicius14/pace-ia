package com.paceai.core.domain.training.session;

import com.paceai.core.domain.training.SessionId;
import com.paceai.core.domain.training.TrainingPlanId;
import java.time.LocalDate;
import java.util.Objects;

public final class Session {

    private final SessionId id;
    private final TrainingPlanId planId;
    private final LocalDate scheduledDate;
    private final SessionType type;
    private final SessionStatus status;
    private final String details;
    private final Long stravaActivityId; // Could be wrapped, but keeping as Long for now per strictness balance

    private Session(Builder builder) {
        this.id = Objects.requireNonNull(builder.id);
        this.planId = Objects.requireNonNull(builder.planId);
        this.scheduledDate = Objects.requireNonNull(builder.scheduledDate);
        this.type = Objects.requireNonNull(builder.type);
        this.status = Objects.requireNonNull(builder.status);
        this.details = builder.details;
        this.stravaActivityId = builder.stravaActivityId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public SessionId id() {
        return id;
    }

    public TrainingPlanId planId() {
        return planId;
    }

    public LocalDate scheduledDate() {
        return scheduledDate;
    }

    public SessionType type() {
        return type;
    }

    public SessionStatus status() {
        return status;
    }

    public String details() {
        return details;
    }

    public Long stravaActivityId() {
        return stravaActivityId;
    }

    public Session markAsCompleted(Long stravaActivityId) {
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
        private SessionId id;
        private TrainingPlanId planId;
        private LocalDate scheduledDate;
        private SessionType type;
        private SessionStatus status = SessionStatus.PENDING;
        private String details;
        private Long stravaActivityId;

        private Builder() {}

        public Builder id(SessionId id) {
            this.id = id;
            return this;
        }

        public Builder planId(TrainingPlanId planId) {
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