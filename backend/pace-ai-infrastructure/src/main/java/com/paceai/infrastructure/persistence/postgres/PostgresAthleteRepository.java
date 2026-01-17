package com.paceai.infrastructure.persistence.postgres;

import com.paceai.domain.athlete.Athlete;
import com.paceai.domain.ports.AthleteRepository;
import com.paceai.infrastructure.persistence.postgres.entity.AthleteEntity;
import com.paceai.infrastructure.persistence.postgres.mappers.AthleteJpaMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PostgresAthleteRepository implements AthleteRepository {

    private final JpaAthleteRepository jpaAthleteRepository;
    private final AthleteJpaMapper athleteJpaMapper;

    public PostgresAthleteRepository(JpaAthleteRepository jpaAthleteRepository, AthleteJpaMapper athleteJpaMapper) {
        this.jpaAthleteRepository = jpaAthleteRepository;
        this.athleteJpaMapper = athleteJpaMapper;
    }

    @Override
    public Athlete save(Athlete athlete) {
        AthleteEntity entity = athleteJpaMapper.toJpaEntity(athlete);
        AthleteEntity savedEntity = jpaAthleteRepository.save(entity);
        return athleteJpaMapper.toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Athlete> findById(UUID id) {
        return jpaAthleteRepository.findById(id)
                .map(athleteJpaMapper::toDomainEntity);
    }

    @Override
    public Optional<Athlete> findByStravaId(Long stravaId) {
        return jpaAthleteRepository.findByStravaId(stravaId)
                .map(athleteJpaMapper::toDomainEntity);
    }

    @Override
    public Optional<Athlete> findByEmail(String email) {
        return jpaAthleteRepository.findByEmail(email)
                .map(athleteJpaMapper::toDomainEntity);
    }

    @Override
    public void deleteById(UUID id) {
        jpaAthleteRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaAthleteRepository.existsById(id);
    }
}
