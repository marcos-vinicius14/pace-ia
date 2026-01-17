package com.paceai.domain.athlete;

import java.math.BigDecimal;

/**
 * Athlete Profile Value Object.
 * <p>
 * Contains physiological data and performance metrics for an athlete.
 * </p>
 */
public record Profile(
        BigDecimal vdotScore,
        Integer maxHeartRate,
        Integer restingHeartRate,
        Integer weeklyMileage
) {
    public Profile {
        if (vdotScore != null && (vdotScore.compareTo(BigDecimal.ZERO) < 0 || vdotScore.compareTo(new BigDecimal("85")) > 0)) {
            throw new IllegalArgumentException("VDOT score must be between 0 and 85");
        }
        if (maxHeartRate != null && (maxHeartRate < 100 || maxHeartRate > 220)) {
            throw new IllegalArgumentException("Max heart rate must be between 100 and 220");
        }
    }

    public static Profile empty() {
        return new Profile(null, null, null, null);
    }
}
