package com.paceai.core.domain.athlete;

import com.paceai.core.domain.shared.Email;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AthleteTests {

    @Test
    void shouldCreateAthlete() {
        Email email = Email.of("test@example.com");

        Athlete athlete = Athlete.create(
                12345L,
                email,
                null,
                "access",
                "refresh"
        );

        assertThat(athlete.email()).isEqualTo(email);
        assertThat(athlete.stravaId()).isEqualTo(12345L);
    }

    @Test
    void shouldUpdateStravaTokens() {
        AthleteId id = AthleteId.create();
        Athlete athlete = Athlete.reconstitute(
                id,
                12345L,
                Email.of("test@example.com"),
                null,
                "old_access",
                "old_refresh"
        );

        Athlete updated = athlete.updateStravaTokens("new_access", "new_refresh");

        assertThat(updated.stravaAccessToken()).isEqualTo("new_access");
        assertThat(updated.stravaRefreshToken()).isEqualTo("new_refresh");
        assertThat(updated.id()).isEqualTo(id);
        // Ensure immutability
        assertThat(athlete.stravaAccessToken()).isEqualTo("old_access");
    }
}
