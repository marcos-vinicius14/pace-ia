package com.paceai.domain.athlete;

import com.paceai.domain.shared.Email;

import java.util.UUID;

/**
 * Athlete Domain Aggregate Root.
 * <p>
 * Represents an athlete with their profile and authentication data.
 * Immutable entity following Rich Domain Model principles.
 * </p>
 */
public final class Athlete {

    private final UUID id;
    private final Long stravaId;
    private final Email email;
    private final Profile profile;
    private final String stravaAccessToken;
    private final String stravaRefreshToken;

    private Athlete(Builder builder) {
        this.id = builder.id;
        this.stravaId = builder.stravaId;
        this.email = builder.email;
        this.profile = builder.profile;
        this.stravaAccessToken = builder.stravaAccessToken;
        this.stravaRefreshToken = builder.stravaRefreshToken;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public Long getStravaId() {
        return stravaId;
    }

    public Email getEmail() {
        return email;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getStravaAccessToken() {
        return stravaAccessToken;
    }

    public String getStravaRefreshToken() {
        return stravaRefreshToken;
    }

    // Domain Behavior
    public Athlete updateStravaTokens(String accessToken, String refreshToken) {
        return Athlete.builder()
                .id(this.id)
                .stravaId(this.stravaId)
                .email(this.email)
                .profile(this.profile)
                .stravaAccessToken(accessToken)
                .stravaRefreshToken(refreshToken)
                .build();
    }

    public static final class Builder {
        private UUID id;
        private Long stravaId;
        private Email email;
        private Profile profile;
        private String stravaAccessToken;
        private String stravaRefreshToken;

        private Builder() {}

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder stravaId(Long stravaId) {
            this.stravaId = stravaId;
            return this;
        }

        public Builder email(Email email) {
            this.email = email;
            return this;
        }

        public Builder profile(Profile profile) {
            this.profile = profile;
            return this;
        }

        public Builder stravaAccessToken(String stravaAccessToken) {
            this.stravaAccessToken = stravaAccessToken;
            return this;
        }

        public Builder stravaRefreshToken(String stravaRefreshToken) {
            this.stravaRefreshToken = stravaRefreshToken;
            return this;
        }

        public Athlete build() {
            return new Athlete(this);
        }
    }
}
