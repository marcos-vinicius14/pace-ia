package com.paceai.domain.athlete;

import com.paceai.domain.shared.Email;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AthleteTests {

    @Test
    void shouldCreateAthlete() {
        AthleteId id = AthleteId.create();
        Email email = Email.of("test@example.com");
        
        Athlete athlete = Athlete.builder()
                .id(id)
                .email(email)
                .stravaId(12345L)
                .build();

        assertThat(athlete.id()).isEqualTo(id);
        assertThat(athlete.email()).isEqualTo(email);
        assertThat(athlete.stravaId()).isEqualTo(12345L);
    }

    @Test
    void shouldUpdateStravaTokens() {
        AthleteId id = AthleteId.create();
        Athlete athlete = Athlete.builder()
                .id(id)
                .email(Email.of("test@example.com"))
                .stravaAccessToken("old_access")
                .stravaRefreshToken("old_refresh")
                .build();

        Athlete updated = athlete.updateStravaTokens("new_access", "new_refresh");

        assertThat(updated.stravaAccessToken()).isEqualTo("new_access");
        assertThat(updated.stravaRefreshToken()).isEqualTo("new_refresh");
        assertThat(updated.id()).isEqualTo(id);
        // Ensure immutability
        assertThat(athlete.stravaAccessToken()).isEqualTo("old_access");
    }
}
