package com.paceai.infrastructure.persistence.postgres;

import com.paceai.domain.athlete.Athlete;
import com.paceai.domain.ports.AthleteRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * PostgreSQL implementation of the AthleteRepository Port.
 */
@Repository
public class PostgresAthleteRepository implements AthleteRepository {

    // TODO: Inject JpaAthleteRepository and AthleteEntityMapper

    @Override
    public Athlete save(Athlete athlete) {
        // TODO: Implement save
        return null;
    }

    @Override
    public Optional<Athlete> findById(UUID id) {
        // TODO: Implement find by ID
        return Optional.empty();
    }

    @Override
    public Optional<Athlete> findByStravaId(Long stravaId) {
        // TODO: Implement find by Strava ID
        return Optional.empty();
    }

    @Override
    public Optional<Athlete> findByEmail(String email) {
        // TODO: Implement find by email
        return Optional.empty();
    }

    @Override
    public void deleteById(UUID id) {
        // TODO: Implement delete
    }

    @Override
    public boolean existsById(UUID id) {
        // TODO: Implement exists check
        return false;
    }
}
