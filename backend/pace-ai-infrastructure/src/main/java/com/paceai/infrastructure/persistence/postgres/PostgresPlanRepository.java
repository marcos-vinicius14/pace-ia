package com.paceai.infrastructure.persistence.postgres;

import com.paceai.domain.ports.PlanRepository;
import com.paceai.domain.training.Plan;
import com.paceai.domain.training.Session;
import com.paceai.domain.exceptions.EntityNotFoundException;
import com.paceai.infrastructure.persistence.postgres.entity.SessionEntity;
import com.paceai.infrastructure.persistence.postgres.mappers.PlanJpaMapper;
import com.paceai.infrastructure.persistence.postgres.mappers.SessionJpaMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class PostgresPlanRepository implements PlanRepository {

    private final JpaPlanRepository jpaPlanRepository;
    private final PlanJpaMapper planJpaMapper;
    private final SessionJpaMapper sessionJpaMapper;

    public PostgresPlanRepository(JpaPlanRepository jpaPlanRepository,
                                  PlanJpaMapper planJpaMapper,
                                  SessionJpaMapper sessionJpaMapper) {
        this.jpaPlanRepository = jpaPlanRepository;
        this.planJpaMapper = planJpaMapper;
        this.sessionJpaMapper = sessionJpaMapper;
    }

    @Override
    @Transactional
    public Plan save(Plan plan) {
        PlanEntity planEntity = planJpaMapper.toJpaEntity(plan);

        if (planEntity.getId() == null) {
            return saveNewPlan(planEntity, plan.getSessions());
        } else {
            return updateExistingPlan(planEntity, plan.getSessions());
        }
    }

    private Plan saveNewPlan(PlanEntity planEntity, List<Session> newSessions) {
        PlanEntity savedPlan = jpaPlanRepository.save(planEntity);

        if (newSessions != null && !newSessions.isEmpty()) {
            newSessions.forEach(session -> {
                SessionEntity entity = sessionJpaMapper.toJpaEntity(session);
                entity.setPlan(savedPlan);
                savedPlan.getSessions().add(entity);
            });
        }

        return planJpaMapper.toDomainEntity(savedPlan, convertSessionsToDomain(savedPlan.getSessions()));
    }

    private Plan updateExistingPlan(PlanEntity planEntity, List<Session> newSessions) {
        PlanEntity existingPlan = jpaPlanRepository.findById(planEntity.getId())
                .orElseThrow(() -> new EntityNotFoundException("Plan", planEntity.getId().toString()));

        updatePlanFields(existingPlan, planEntity);

        if (newSessions != null && !newSessions.isEmpty()) {
            reconcileSessions(existingPlan, newSessions);
        }

        return planJpaMapper.toDomainEntity(existingPlan, convertSessionsToDomain(existingPlan.getSessions()));
    }

    private void updatePlanFields(PlanEntity target, PlanEntity source) {
        if (source.getGoalDistance() != null) {
            target.setGoalDistance(source.getGoalDistance());
        }
        if (source.getStatus() != null) {
            target.setStatus(source.getStatus());
        }
        if (source.getAiModelVersion() != null) {
            target.setAiModelVersion(source.getAiModelVersion());
        }
    }

    private void reconcileSessions(PlanEntity planEntity, List<Session> newSessions) {
        List<SessionEntity> existingSessions = planEntity.getSessions() != null
                ? planEntity.getSessions()
                : new ArrayList<>();

        Map<UUID, SessionEntity> existingMap = existingSessions.stream()
                .filter(s -> s.getId() != null)
                .collect(Collectors.toMap(SessionEntity::getId, Function.identity()));

        List<SessionEntity> toRemove = existingSessions.stream()
                .filter(s -> s.getId() != null && newSessions.stream()
                        .noneMatch(ns -> ns.getId() != null && ns.getId().equals(s.getId())))
                .collect(Collectors.toList());

        planEntity.getSessions().removeAll(toRemove);

        newSessions.forEach(ns -> {
            if (ns.getId() == null) {
                SessionEntity entity = sessionJpaMapper.toJpaEntity(ns);
                entity.setPlan(planEntity);
                planEntity.getSessions().add(entity);
            } else {
                SessionEntity existing = existingMap.get(ns.getId());
                if (existing != null) {
                    sessionJpaMapper.updateEntityFromDomain(existing, ns);
                }
            }
        });
    }

    private List<Session> convertSessionsToDomain(List<SessionEntity> sessionEntities) {
        if (sessionEntities == null || sessionEntities.isEmpty()) {
            return List.of();
        }

        return sessionEntities.stream()
                .map(sessionJpaMapper::toDomainEntity)
                .sorted(java.util.Comparator.comparing(Session::getScheduledDate))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Plan> findById(UUID id) {
        return jpaPlanRepository.findByIdWithSessions(id)
                .map(entity -> planJpaMapper.toDomainEntity(entity, convertSessionsToDomain(entity.getSessions())));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Plan> findByAthleteId(UUID athleteId) {
        List<com.paceai.infrastructure.persistence.postgres.PlanEntity> planEntities = jpaPlanRepository.findByAthleteIdWithSessions(athleteId);

        return planEntities.stream()
                .map(entity -> planJpaMapper.toDomainEntity(entity, convertSessionsToDomain(entity.getSessions())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        jpaPlanRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaPlanRepository.existsById(id);
    }
}
