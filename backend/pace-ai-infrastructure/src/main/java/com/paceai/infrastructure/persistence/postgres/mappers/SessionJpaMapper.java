package com.paceai.infrastructure.persistence.postgres.mappers;

import com.paceai.domain.training.Session;
import com.paceai.domain.training.SessionStatus;
import com.paceai.domain.training.SessionType;
import com.paceai.infrastructure.persistence.postgres.entity.SessionEntity;
import org.springframework.stereotype.Component;

@Component
public class SessionJpaMapper {

    public SessionEntity toJpaEntity(Session session) {
        if (session == null) {
            return null;
        }

        SessionEntity entity = new SessionEntity();
        entity.setId(session.getId());
        // Plan is set by the parent PlanEntity or Repository
        entity.setScheduledDate(session.getScheduledDate());
        entity.setType(mapSessionType(session.getType()));
        entity.setStatus(mapSessionStatus(session.getStatus()));
        entity.setDetailsJsonb(session.getDetails());
        entity.setStravaActivityId(session.getStravaActivityId());

        return entity;
    }

    public Session toDomainEntity(SessionEntity entity) {
        if (entity == null) {
            return null;
        }

        return Session.builder()
                .id(entity.getId())
                .planId(entity.getPlan() != null ? entity.getPlan().getId() : null)
                .scheduledDate(entity.getScheduledDate())
                .type(mapSessionTypeJpa(entity.getType()))
                .status(mapSessionStatusJpa(entity.getStatus()))
                .details(entity.getDetailsJsonb())
                .stravaActivityId(entity.getStravaActivityId())
                .build();
    }

    public void updateEntityFromDomain(SessionEntity entity, Session session) {
        if (entity == null || session == null) {
            return;
        }
        entity.setScheduledDate(session.getScheduledDate());
        entity.setType(mapSessionType(session.getType()));
        entity.setStatus(mapSessionStatus(session.getStatus()));
        entity.setDetailsJsonb(session.getDetails());
        entity.setStravaActivityId(session.getStravaActivityId());
    }

    public SessionEntity.SessionTypeJpa mapSessionType(SessionType type) {
        return switch (type) {
            case REST -> SessionEntity.SessionTypeJpa.REST;
            case EASY -> SessionEntity.SessionTypeJpa.EASY;
            case LONG_RUN -> SessionEntity.SessionTypeJpa.LONG_RUN;
            case TEMPO -> SessionEntity.SessionTypeJpa.TEMPO;
            case INTERVAL -> SessionEntity.SessionTypeJpa.INTERVAL;
            case FARTLEK -> SessionEntity.SessionTypeJpa.FARTLEK;
            case RECOVERY -> SessionEntity.SessionTypeJpa.RECOVERY;
            case RACE -> SessionEntity.SessionTypeJpa.RACE;
        };
    }

    public SessionType mapSessionTypeJpa(SessionEntity.SessionTypeJpa typeJpa) {
        return switch (typeJpa) {
            case REST -> SessionType.REST;
            case EASY -> SessionType.EASY;
            case LONG_RUN -> SessionType.LONG_RUN;
            case TEMPO -> SessionType.TEMPO;
            case INTERVAL -> SessionType.INTERVAL;
            case FARTLEK -> SessionType.FARTLEK;
            case RECOVERY -> SessionType.RECOVERY;
            case RACE -> SessionType.RACE;
        };
    }

    public SessionEntity.SessionStatusJpa mapSessionStatus(SessionStatus status) {
        return switch (status) {
            case PENDING -> SessionEntity.SessionStatusJpa.PENDING;
            case COMPLETED -> SessionEntity.SessionStatusJpa.COMPLETED;
            case MISSED -> SessionEntity.SessionStatusJpa.MISSED;
            case SKIPPED -> SessionEntity.SessionStatusJpa.SKIPPED;
        };
    }

    public SessionStatus mapSessionStatusJpa(SessionEntity.SessionStatusJpa statusJpa) {
        return switch (statusJpa) {
            case PENDING -> SessionStatus.PENDING;
            case COMPLETED -> SessionStatus.COMPLETED;
            case MISSED -> SessionStatus.MISSED;
            case SKIPPED -> SessionStatus.SKIPPED;
        };
    }
}
