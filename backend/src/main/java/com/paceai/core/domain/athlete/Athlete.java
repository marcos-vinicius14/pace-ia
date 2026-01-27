package com.paceai.core.domain.athlete;

import java.util.Objects;

import com.paceai.core.domain.shared.Email;

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

    private Athlete(
            AthleteId id,
            Long stravaId,
            Email email,
            Profile profile,
            String stravaAccessToken,
            String stravaRefreshToken
    ) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.stravaId = stravaId;
        this.email = email;
        this.profile = profile;
        this.stravaAccessToken = stravaAccessToken;
        this.stravaRefreshToken = stravaRefreshToken;
    }

    public static Athlete create(
            Long stravaId,
            Email email,
            Profile profile,
            String stravaAccessToken,
            String stravaRefreshToken
    ) {
        return new Athlete(
                AthleteId.create(),
                stravaId,
                email,
                profile,
                stravaAccessToken,
                stravaRefreshToken
        );
    }

    public static Athlete reconstitute(
            AthleteId id,
            Long stravaId,
            Email email,
            Profile profile,
            String stravaAccessToken,
            String stravaRefreshToken
    ) {
        return new Athlete(
                id,
                stravaId,
                email,
                profile,
                stravaAccessToken,
                stravaRefreshToken
        );
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
        return Athlete.reconstitute(
                this.id,
                this.stravaId,
                this.email,
                this.profile,
                accessToken,
                refreshToken
        );
    }
}
