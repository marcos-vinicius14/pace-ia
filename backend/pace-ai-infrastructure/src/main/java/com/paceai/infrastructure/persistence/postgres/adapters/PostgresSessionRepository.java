package com.paceai.infrastructure.persistence.postgres.adapters;

import com.paceai.domain.ports.SessionRepository;
import com.paceai.domain.training.SessionId;
import com.paceai.domain.training.TrainingPlanId;
import com.paceai.domain.training.session.Session;
import com.paceai.infrastructure.persistence.postgres.entity.SessionEntity;
import com.paceai.infrastructure.persistence.postgres.mappers.SessionJpaMapper;
import com.paceai.infrastructure.persistence.postgres.repositories.JpaSessionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class PostgresSessionRepository implements SessionRepository {

    private final JpaSessionRepository jpaSessionRepository;
    private final SessionJpaMapper sessionJpaMapper;

    public PostgresSessionRepository(JpaSessionRepository jpaSessionRepository, SessionJpaMapper sessionJpaMapper) {
        this.jpaSessionRepository = jpaSessionRepository;
        this.sessionJpaMapper = sessionJpaMapper;
    }

    @Override
    public Session save(Session session) {
        SessionEntity entity = sessionJpaMapper.toJpaEntity(session);
        SessionEntity savedEntity = jpaSessionRepository.save(entity);
        return sessionJpaMapper.toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Session> findById(SessionId id) {
        return jpaSessionRepository.findById(id.value())
                .map(sessionJpaMapper::toDomainEntity);
    }

    @Override
    public List<Session> findByPlanId(TrainingPlanId planId) {
        return jpaSessionRepository.findByPlanIdOrderByScheduledDateAsc(planId.value()).stream()
                .map(sessionJpaMapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(SessionId id) {
        jpaSessionRepository.deleteById(id.value());
    }

    @Override
    public boolean existsById(SessionId id) {
        return jpaSessionRepository.existsById(id.value());
    }
}
