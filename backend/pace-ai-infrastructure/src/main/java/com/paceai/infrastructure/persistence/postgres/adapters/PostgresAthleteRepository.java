package com.paceai.infrastructure.persistence.postgres.adapters;

import com.paceai.domain.athlete.Athlete;
import com.paceai.domain.athlete.AthleteId;
import com.paceai.domain.ports.AthleteRepository;
import com.paceai.infrastructure.persistence.postgres.entity.AthleteEntity;
import com.paceai.infrastructure.persistence.postgres.mappers.AthleteJpaMapper;
import com.paceai.infrastructure.persistence.postgres.repositories.JpaAthleteRepository;
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
    public Optional<Athlete> findById(AthleteId id) {
        return jpaAthleteRepository.findById(id.value())
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
    public void deleteById(AthleteId id) {
        jpaAthleteRepository.deleteById(id.value());
    }

    @Override
    public boolean existsById(AthleteId id) {
        return jpaAthleteRepository.existsById(id.value());
    }
}
