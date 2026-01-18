package com.paceai.domain.ports;

import java.util.List;
import java.util.Optional;

import com.paceai.domain.training.session.Session;
import com.paceai.domain.training.SessionId;
import com.paceai.domain.training.TrainingPlanId;

public interface SessionRepository {

    Session save(Session session);
    Optional<Session> findById(SessionId id);
    List<Session> findByPlanId(TrainingPlanId planId);
    void deleteById(SessionId id);
    boolean existsById(SessionId id);
}
