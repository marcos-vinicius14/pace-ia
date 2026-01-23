package com.paceai.core.domain.athlete;

import com.paceai.core.domain.shared.Email;

import java.util.Objects;

/**
 * Athlete Domain Aggregate Root.
 * <p>
 * Represents an athlete with their profile and authentication data.
 * Immutable entity following Rich Domain Model principles.
 * </p>
 */
public final class Athlete {

    private final AthleteId id;
    private final Long stravaId;
    private final Email email;
    private final Profile profile;
    private final String stravaAccessToken;
    private final String stravaRefreshToken;

    private Athlete(Builder builder) {
        this.id = Objects.requireNonNull(builder.id);
        this.stravaId = builder.stravaId;
        this.email = builder.email;
        this.profile = builder.profile;
        this.stravaAccessToken = builder.stravaAccessToken;
        this.stravaRefreshToken = builder.stravaRefreshToken;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Accessors (Fluent)
    public AthleteId id() {
        return id;
    }

    public Long stravaId() {
        return stravaId;
    }

    public Email email() {
        return email;
    }

    public Profile profile() {
        return profile;
    }

    public String stravaAccessToken() {
        return stravaAccessToken;
    }

    public String stravaRefreshToken() {
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
        private AthleteId id;
        private Long stravaId;
        private Email email;
        private Profile profile;
        private String stravaAccessToken;
        private String stravaRefreshToken;

        private Builder() {}

        public Builder id(AthleteId id) {
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