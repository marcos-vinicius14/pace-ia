package com.paceai.infrastructure.persistence.postgres.mappers;

import com.paceai.domain.athlete.Athlete;
import com.paceai.domain.athlete.Profile;
import com.paceai.domain.shared.Email;
import com.paceai.infrastructure.persistence.postgres.entity.AthleteEntity;
import org.springframework.stereotype.Component;

@Component
public class AthleteJpaMapper {

    public AthleteEntity toJpaEntity(Athlete athlete) {
        if (athlete == null) {
            return null;
        }

        AthleteEntity entity = new AthleteEntity();
        entity.setId(athlete.getId());
        entity.setStravaId(athlete.getStravaId());
        entity.setStravaAccessToken(athlete.getStravaAccessToken());
        entity.setStravaRefreshToken(athlete.getStravaRefreshToken());
        entity.setEmail(athlete.getEmail() != null ? athlete.getEmail().getValue() : null);

        if (athlete.getProfile() != null) {
            entity.setVdotScore(athlete.getProfile().vdotScore() != null ? athlete.getProfile().vdotScore().doubleValue() : null);
            entity.setMaxHeartRate(athlete.getProfile().maxHeartRate());
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
                null,
                null
        );

        return Athlete.builder()
                .id(entity.getId())
                .stravaId(entity.getStravaId())
                .email(email)
                .profile(profile)
                .stravaAccessToken(entity.getStravaAccessToken())
                .stravaRefreshToken(entity.getStravaRefreshToken())
                .build();
    }
}
