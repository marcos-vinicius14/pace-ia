package com.paceai.domain.ports;

import com.paceai.domain.athlete.Athlete;

import java.util.Optional;
import java.util.UUID;

/**
 * Athlete Repository Port (Interface).
 * <p>
 * Defines the contract for persisting and retrieving Athletes.
 * </p>
 */
public interface AthleteRepository {

    Athlete save(Athlete athlete);

    Optional<Athlete> findById(UUID id);

    Optional<Athlete> findByStravaId(Long stravaId);

    Optional<Athlete> findByEmail(String email);

    void deleteById(UUID id);

    boolean existsById(UUID id);
}
