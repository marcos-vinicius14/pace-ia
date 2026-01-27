package com.paceai.infra.persistence.postgres.mappers;

import org.springframework.stereotype.Component;

import com.paceai.core.domain.athlete.Athlete;
import com.paceai.core.domain.athlete.Profile;
import com.paceai.core.domain.shared.Email;
import com.paceai.infra.persistence.postgres.entity.AthleteEntity;

@Component
public class AthleteJpaMapper {

    public AthleteEntity toJpaEntity(Athlete athlete) {
        if (athlete == null) {
            return null;
        }

        AthleteEntity entity = new AthleteEntity();
        entity.setId(athlete.id().value());
        entity.setStravaId(athlete.stravaId());
        entity.setStravaAccessToken(athlete.stravaAccessToken());
        entity.setStravaRefreshToken(athlete.stravaRefreshToken());
        entity.setEmail(athlete.email() != null ? athlete.email().getValue() : null);

        if (athlete.profile() != null) {
            entity.setVdotScore(athlete.profile().vdotScore());
            entity.setMaxHeartRate(athlete.profile().maxHeartRate());
            entity.setRestingHeartRate(athlete.profile().restingHeartRate());
            entity.setWeeklyMileage(athlete.profile().weeklyMileage());
        }

        return entity;
    }

    public Athlete toDomainEntity(AthleteEntity entity) {
        if (entity == null) {
            return null;
        }

        Email email = entity.getEmail() != null ? Email.of(entity.getEmail()) : null;
        Profile profile = new Profile(
                entity.getVdotScore(),
                entity.getMaxHeartRate(),
                entity.getRestingHeartRate(),
                entity.getWeeklyMileage()
        );

        return Athlete.reconstitute(
                com.paceai.core.domain.athlete.AthleteId.of(entity.getId()),
                entity.getStravaId(),
                email,
                profile,
                entity.getStravaAccessToken(),
                entity.getStravaRefreshToken()
        );
    }
}
