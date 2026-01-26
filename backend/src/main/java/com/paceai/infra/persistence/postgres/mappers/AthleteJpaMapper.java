package com.paceai.infra.persistence.postgres.mappers;

import com.paceai.core.domain.athlete.Athlete;
import com.paceai.core.domain.athlete.Profile;
import com.paceai.core.domain.shared.Email;
import com.paceai.infra.persistence.postgres.entity.AthleteEntity;
import org.springframework.stereotype.Component;

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
            entity.setVdotScore(athlete.profile().vdotScore() != null ? athlete.profile().vdotScore().doubleValue() : null);
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
                entity.getVdotScore() != null ? java.math.BigDecimal.valueOf(entity.getVdotScore()) : null,
                entity.getMaxHeartRate(),
                entity.getRestingHeartRate(),
                entity.getWeeklyMileage()
        );

        return Athlete.builder()
                .id(com.paceai.core.domain.athlete.AthleteId.of(entity.getId()))
                .stravaId(entity.getStravaId())
                .email(email)
                .profile(profile)
                .stravaAccessToken(entity.getStravaAccessToken())
                .stravaRefreshToken(entity.getStravaRefreshToken())
                .build();
    }
}
