package com.paceai.infra.persistence.postgres.adapters;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.paceai.core.domain.athlete.AthleteId;
import com.paceai.core.domain.shared.Result;
import com.paceai.core.domain.training.TrainingPlanId;
import com.paceai.core.domain.training.plan.TrainingPlan;
import com.paceai.core.domain.training.session.Session;
import com.paceai.core.domain.training.session.Sessions;
import com.paceai.core.gateways.TrainingPlanRepository;
import com.paceai.infra.persistence.postgres.entity.SessionEntity;
import com.paceai.infra.persistence.postgres.entity.TrainingPlanEntity;
import com.paceai.infra.persistence.postgres.mappers.SessionJpaMapper;
import com.paceai.infra.persistence.postgres.mappers.TrainingPlanJpaMapper;
import com.paceai.infra.persistence.postgres.repositories.JpaTrainingPlanRepository;

@Repository
public class PostgresTrainingPlanRepository implements TrainingPlanRepository {

    private final JpaTrainingPlanRepository jpaTrainingPlanRepository;
    private final TrainingPlanJpaMapper trainingPlanJpaMapper;
    private final SessionJpaMapper sessionJpaMapper;
    private final EntityManager entityManager;

    public PostgresTrainingPlanRepository(
            JpaTrainingPlanRepository jpaTrainingPlanRepository,
            TrainingPlanJpaMapper trainingPlanJpaMapper,
            SessionJpaMapper sessionJpaMapper,
            EntityManager entityManager
    ) {
        this.jpaTrainingPlanRepository = jpaTrainingPlanRepository;
        this.trainingPlanJpaMapper = trainingPlanJpaMapper;
        this.sessionJpaMapper = sessionJpaMapper;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Result<TrainingPlan> save(TrainingPlan plan) {
        TrainingPlanEntity planEntity = trainingPlanJpaMapper.toJpaEntity(plan);
        return isNew(planEntity)
            ? Result.success(saveNewPlan(planEntity, plan.sessions()))
            : updateExistingPlan(planEntity, plan.sessions());
    }

    private boolean isNew(TrainingPlanEntity entity) {
        return entity.getId() == null || !jpaTrainingPlanRepository.existsById(entity.getId());
    }

    private TrainingPlan saveNewPlan(TrainingPlanEntity planEntity, Sessions newSessions) {
        addSessionsToPlan(planEntity, newSessions);
        entityManager.persist(planEntity);
        entityManager.flush();
        return trainingPlanJpaMapper.toDomainEntity(planEntity, convertSessionsToDomain(planEntity.getSessions()));
    }

    private void addSessionsToPlan(TrainingPlanEntity planEntity, Sessions sessions) {
        if (sessions == null || sessions.count() == 0) return;
        
        sessions.forEach(session -> {
            SessionEntity entity = sessionJpaMapper.toJpaEntity(session);
            entity.setPlan(planEntity);
            planEntity.getSessions().add(entity);
        });
    }

    private Result<TrainingPlan> updateExistingPlan(TrainingPlanEntity planEntity, Sessions newSessions) {
        Optional<TrainingPlanEntity> existingPlan = jpaTrainingPlanRepository.findById(planEntity.getId());
        if (existingPlan.isEmpty()) {
            return Result.failure("TrainingPlan não encontrado para atualização: " + planEntity.getId());
        }

        TrainingPlanEntity planToUpdate = existingPlan.get();
        updatePlanFields(planToUpdate, planEntity);
        reconcileSessions(planToUpdate, newSessions);

        return Result.success(
                trainingPlanJpaMapper.toDomainEntity(planToUpdate, convertSessionsToDomain(planToUpdate.getSessions()))
        );
    }

    private void updatePlanFields(TrainingPlanEntity target, TrainingPlanEntity source) {
        updateIfNotNull(source.getGoalDistance(), target::setGoalDistance);
        updateIfNotNull(source.getStatus(), target::setStatus);
        updateIfNotNull(source.getAiModelVersion(), target::setAiModelVersion);
        updateIfNotNull(source.getWeeklyVolumeKm(), target::setWeeklyVolumeKm);
        updateIfNotNull(source.getStartDate(), target::setStartDate);
        updateIfNotNull(source.getRaceDate(), target::setRaceDate);
    }

    private <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) setter.accept(value);
    }

    private void reconcileSessions(TrainingPlanEntity planEntity, Sessions newSessions) {
        if (newSessions == null) return;

        List<SessionEntity> existingSessions = Optional.ofNullable(planEntity.getSessions())
                .orElse(new ArrayList<>());
        
        removeObsoleteSessions(planEntity, existingSessions, newSessions);
        upsertSessions(planEntity, existingSessions, newSessions);
    }

    private void removeObsoleteSessions(TrainingPlanEntity planEntity, List<SessionEntity> existingSessions, Sessions newSessions) {
        List<SessionEntity> toRemove = existingSessions.stream()
                .filter(s -> shouldRemoveSession(s, newSessions))
                .collect(Collectors.toList());
        
        planEntity.getSessions().removeAll(toRemove);
    }

    private boolean shouldRemoveSession(SessionEntity current, Sessions newSessions) {
        return current.getId() != null && newSessions.asList().stream()
                .noneMatch(ns -> ns.id() != null && ns.id().value().equals(current.getId()));
    }

    private void upsertSessions(TrainingPlanEntity planEntity, List<SessionEntity> existingSessions, Sessions newSessions) {
        Map<UUID, SessionEntity> existingMap = existingSessions.stream()
                .filter(s -> s.getId() != null)
                .collect(Collectors.toMap(SessionEntity::getId, Function.identity()));

        newSessions.forEach(ns -> processSessionUpsert(planEntity, existingMap, ns));
    }

    private void processSessionUpsert(TrainingPlanEntity planEntity, Map<UUID, SessionEntity> existingMap, Session session) {
        if (isNewSession(session)) {
            addNewSession(planEntity, session);
        } else {
            updateExistingSession(existingMap, session);
        }
    }

    private boolean isNewSession(Session session) {
        return session.id() == null || session.id().value() == null;
    }

    private void addNewSession(TrainingPlanEntity planEntity, Session session) {
        SessionEntity entity = sessionJpaMapper.toJpaEntity(session);
        entity.setPlan(planEntity);
        planEntity.getSessions().add(entity);
    }

    private void updateExistingSession(Map<UUID, SessionEntity> existingMap, Session session) {
        SessionEntity existing = existingMap.get(session.id().value());
        if (existing != null) {
            sessionJpaMapper.updateEntityFromDomain(existing, session);
        }
    }

    private Sessions convertSessionsToDomain(List<SessionEntity> sessionEntities) {
        if (sessionEntities == null || sessionEntities.isEmpty()) {
            return Sessions.empty();
        }

        List<Session> sessions = sessionEntities.stream()
                .map(sessionJpaMapper::toDomainEntity)
                .sorted(Comparator.comparing(Session::scheduledDate))
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
