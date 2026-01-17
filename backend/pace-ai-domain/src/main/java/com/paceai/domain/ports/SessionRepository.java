package com.paceai.domain.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.paceai.domain.training.Session;

public interface SessionRepository {

    Session save(Session session);
    Optional<Session> findById(UUID id);
    List<Session> findByPlanId(UUID planId);
    void deleteById(UUID id);
    boolean existsById(UUID id);
}
