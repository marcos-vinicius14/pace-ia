package com.paceai.infrastructure.persistence.postgres;

import com.paceai.domain.ports.SessionRepository;
import com.paceai.domain.training.Session;
import com.paceai.infrastructure.persistence.postgres.entity.SessionEntity;
import com.paceai.infrastructure.persistence.postgres.mappers.SessionJpaMapper;
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
    public Optional<Session> findById(UUID id) {
        return jpaSessionRepository.findById(id)
                .map(sessionJpaMapper::toDomainEntity);
    }

    @Override
    public List<Session> findByPlanId(UUID planId) {
        return jpaSessionRepository.findByPlanIdOrderByScheduledDateAsc(planId).stream()
                .map(sessionJpaMapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaSessionRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaSessionRepository.existsById(id);
    }
}
