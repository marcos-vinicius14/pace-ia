package com.paceai.core.gateways;

import java.util.Optional;

import com.paceai.core.domain.athlete.Athlete;
import com.paceai.core.domain.athlete.AthleteId;

/**
 * Athlete Repository Port (Interface).
 * <p>
 * Defines the contract for persisting and retrieving Athletes.
 * </p>
 */
public interface AthleteRepository {

    Athlete save(Athlete athlete);
    Optional<Athlete> findById(AthleteId id);
    Optional<Athlete> findByStravaId(Long stravaId);
    Optional<Athlete> findByEmail(String email);
    void deleteById(AthleteId id);
    boolean existsById(AthleteId id);

}