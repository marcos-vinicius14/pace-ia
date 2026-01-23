package com.paceai.infrastructure.persistence.postgres;

import com.paceai.domain.athlete.AthleteId;
import com.paceai.domain.exceptions.EntityNotFoundException;
import com.paceai.domain.training.TrainingPlanId;
import com.paceai.domain.ports.TrainingPlanRepository;
import com.paceai.domain.training.plan.TrainingPlan;
import com.paceai.domain.training.session.Session;
import com.paceai.domain.training.session.Sessions;
import com.paceai.infrastructure.persistence.postgres.entity.SessionEntity;
import com.paceai.infrastructure.persistence.postgres.entity.TrainingPlanEntity;
import com.paceai.infrastructure.persistence.postgres.mappers.SessionJpaMapper;
import com.paceai.infrastructure.persistence.postgres.mappers.TrainingPlanJpaMapper;
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
public class PostgresTrainingPlanRepository implements TrainingPlanRepository {

    private final JpaTrainingPlanRepository jpaTrainingPlanRepository;
    private final TrainingPlanJpaMapper trainingPlanJpaMapper;
    private final SessionJpaMapper sessionJpaMapper;

    public PostgresTrainingPlanRepository(
            JpaTrainingPlanRepository jpaTrainingPlanRepository,
            TrainingPlanJpaMapper trainingPlanJpaMapper,
            SessionJpaMapper sessionJpaMapper
    ) {
        this.jpaTrainingPlanRepository = jpaTrainingPlanRepository;
        this.trainingPlanJpaMapper = trainingPlanJpaMapper;
        this.sessionJpaMapper = sessionJpaMapper;
    }

    @Override
    @Transactional
    public TrainingPlan save(TrainingPlan plan) {
        TrainingPlanEntity planEntity = trainingPlanJpaMapper.toJpaEntity(plan);

        if (planEntity.getId() == null) {
            return saveNewPlan(planEntity, plan.sessions());
        } else {
            return updateExistingPlan(planEntity, plan.sessions());
        }
    }

    private TrainingPlan saveNewPlan(TrainingPlanEntity planEntity, Sessions newSessions) {
        TrainingPlanEntity savedPlan = jpaTrainingPlanRepository.save(planEntity);

        if (newSessions != null && newSessions.count() > 0) {
            newSessions.forEach(session -> {
                SessionEntity entity = sessionJpaMapper.toJpaEntity(session);
                entity.setPlan(savedPlan);
                savedPlan.getSessions().add(entity);
            });
        }

        return trainingPlanJpaMapper.toDomainEntity(savedPlan, convertSessionsToDomain(savedPlan.getSessions()));
    }

    private TrainingPlan updateExistingPlan(TrainingPlanEntity planEntity, Sessions newSessions) {
        TrainingPlanEntity existingPlan = jpaTrainingPlanRepository.findById(planEntity.getId())
                .orElseThrow(() -> new EntityNotFoundException("TrainingPlan", planEntity.getId().toString()));

        updatePlanFields(existingPlan, planEntity);

        if (newSessions != null) {
            reconcileSessions(existingPlan, newSessions);
        }

        return trainingPlanJpaMapper.toDomainEntity(existingPlan, convertSessionsToDomain(existingPlan.getSessions()));
    }

    private void updatePlanFields(TrainingPlanEntity target, TrainingPlanEntity source) {
        if (source.getGoalDistance() != null) {
            target.setGoalDistance(source.getGoalDistance());
        }
        if (source.getStatus() != null) {
            target.setStatus(source.getStatus());
        }
        if (source.getAiModelVersion() != null) {
            target.setAiModelVersion(source.getAiModelVersion());
        }
        if (source.getWeeklyVolumeKm() != null) {
            target.setWeeklyVolumeKm(source.getWeeklyVolumeKm());
        }
        if (source.getStartDate() != null) {
            target.setStartDate(source.getStartDate());
        }
        if (source.getRaceDate() != null) {
            target.setRaceDate(source.getRaceDate());
        }
    }

    private void reconcileSessions(TrainingPlanEntity planEntity, Sessions newSessions) {
        List<SessionEntity> existingSessions = planEntity.getSessions() != null
                ? planEntity.getSessions()
                : new ArrayList<>();

        Map<UUID, SessionEntity> existingMap = existingSessions.stream()
                .filter(s -> s.getId() != null)
                .collect(Collectors.toMap(SessionEntity::getId, Function.identity()));

        List<SessionEntity> toRemove = existingSessions.stream()
                .filter(s -> s.getId() != null && newSessions.asList().stream()
                        .noneMatch(ns -> ns.id() != null && ns.id().value().equals(s.getId())))
                .collect(Collectors.toList());

        planEntity.getSessions().removeAll(toRemove);

        newSessions.forEach(ns -> {
            if (ns.id() == null || ns.id().value() == null) {
                SessionEntity entity = sessionJpaMapper.toJpaEntity(ns);
                entity.setPlan(planEntity);
                planEntity.getSessions().add(entity);
            } else {
                SessionEntity existing = existingMap.get(ns.id().value());
                if (existing != null) {
                    sessionJpaMapper.updateEntityFromDomain(existing, ns);
                }
            }
        });
    }

    private Sessions convertSessionsToDomain(List<SessionEntity> sessionEntities) {
        if (sessionEntities == null || sessionEntities.isEmpty()) {
            return Sessions.empty();
        }

        List<Session> sessions = sessionEntities.stream()
                .map(sessionJpaMapper::toDomainEntity)
                .sorted(java.util.Comparator.comparing(Session::scheduledDate))
                .collect(Collectors.toList());

        return Sessions.of(sessions);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrainingPlan> findById(TrainingPlanId id) {
        return jpaTrainingPlanRepository.findByIdWithSessions(id.value())
                .map(entity -> trainingPlanJpaMapper.toDomainEntity(entity, convertSessionsToDomain(entity.getSessions())));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingPlan> findByAthleteId(AthleteId athleteId) {
        List<TrainingPlanEntity> planEntities = jpaTrainingPlanRepository.findByAthleteIdWithSessions(athleteId.value());

        return planEntities.stream()
                .map(entity -> trainingPlanJpaMapper.toDomainEntity(entity, convertSessionsToDomain(entity.getSessions())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(TrainingPlanId id) {
        jpaTrainingPlanRepository.deleteById(id.value());
    }

    @Override
    public boolean existsById(TrainingPlanId id) {
        return jpaTrainingPlanRepository.existsById(id.value());
    }
}
