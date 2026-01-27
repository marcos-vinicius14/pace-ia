package com.paceai.infra.persistence.postgres.adapters;

import com.paceai.core.gateways.SessionRepository;
import com.paceai.core.domain.training.SessionId;
import com.paceai.core.domain.training.TrainingPlanId;
import com.paceai.core.domain.training.session.Session;
import com.paceai.infra.persistence.postgres.entity.SessionEntity;
import com.paceai.infra.persistence.postgres.mappers.SessionJpaMapper;
import com.paceai.infra.persistence.postgres.repositories.JpaSessionRepository;
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
        return jpaSessionRepository.findByPlan_IdOrderByScheduledDateAsc(planId.value()).stream()
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
