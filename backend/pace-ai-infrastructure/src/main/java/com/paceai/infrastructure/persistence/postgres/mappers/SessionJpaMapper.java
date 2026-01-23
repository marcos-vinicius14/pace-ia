package com.paceai.infrastructure.persistence.postgres.mappers;

import com.paceai.domain.training.SessionId;
import com.paceai.domain.training.TrainingPlanId;
import com.paceai.domain.training.session.Session;
import com.paceai.domain.training.session.SessionStatus;
import com.paceai.domain.training.session.SessionType;
import com.paceai.infrastructure.persistence.postgres.entity.SessionEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SessionJpaMapper {

    public SessionEntity toJpaEntity(Session session) {
        if (session == null) {
            return null;
        }

        SessionEntity entity = new SessionEntity();
        entity.setId(session.id().value());
        entity.setScheduledDate(session.scheduledDate());
        entity.setType(mapSessionType(session.type()));
        entity.setStatus(mapSessionStatus(session.status()));
        entity.setDetailsJsonb(session.details());
        entity.setStravaActivityId(session.stravaActivityId());

        return entity;
    }

    public Session toDomainEntity(SessionEntity entity) {
        if (entity == null) {
            return null;
        }

        return Session.builder()
                .id(SessionId.of(entity.getId()))
                .planId(entity.getPlan() != null ? TrainingPlanId.of(entity.getPlan().getId()) : null)
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
        entity.setScheduledDate(session.scheduledDate());
        entity.setType(mapSessionType(session.type()));
        entity.setStatus(mapSessionStatus(session.status()));
        entity.setDetailsJsonb(session.details());
        entity.setStravaActivityId(session.stravaActivityId());
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
