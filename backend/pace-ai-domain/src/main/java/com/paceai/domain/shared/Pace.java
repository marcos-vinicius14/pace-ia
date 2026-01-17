package com.paceai.domain.shared;

import java.time.Duration;
import java.util.Objects;

/**
 * Pace Value Object.
 * <p>
 * Immutable value object representing running pace (time per distance unit).
 * </p>
 */
public final class Pace {

    private final Duration timePerKilometer;

    private Pace(Duration timePerKilometer) {
        if (timePerKilometer.isNegative() || timePerKilometer.isZero()) {
            throw new IllegalArgumentException("Pace must be positive");
        }
        this.timePerKilometer = timePerKilometer;
    }

    public static Pace ofMinutesPerKilometer(int minutes, int seconds) {
        return new Pace(Duration.ofMinutes(minutes).plusSeconds(seconds));
    }

    public static Pace ofSecondsPerKilometer(long seconds) {
        return new Pace(Duration.ofSeconds(seconds));
    }

    public Duration getTimePerKilometer() {
        return timePerKilometer;
    }

    public Duration getTimePerMile() {
        long secondsPerKm = timePerKilometer.toSeconds();
        return Duration.ofSeconds((long) (secondsPerKm * 1.60934));
    }

    public double getSpeedKmh() {
        return 3600.0 / timePerKilometer.toSeconds();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pace pace = (Pace) o;
        return Objects.equals(timePerKilometer, pace.timePerKilometer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timePerKilometer);
    }

    @Override
    public String toString() {
        long totalSeconds = timePerKilometer.toSeconds();
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%d:%02d/km", minutes, seconds);
    }
}
