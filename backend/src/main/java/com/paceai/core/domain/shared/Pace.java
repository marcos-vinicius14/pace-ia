package com.paceai.core.domain.shared;

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
        this.timePerKilometer = timePerKilometer;
    }

    public static Result<Pace> create(int minutes, int seconds) {
        if (minutes < 0 || seconds < 0 || (minutes == 0 && seconds == 0)) {
            return Result.failure("Pace must be positive");
        }
        return Result.success(new Pace(Duration.ofMinutes(minutes).plusSeconds(seconds)));
    }

    public static Result<Pace> create(long totalSeconds) {
        if (totalSeconds <= 0) {
            return Result.failure("Pace must be positive");
        }
        return Result.success(new Pace(Duration.ofSeconds(totalSeconds)));
    }

    /**
     * @deprecated Use create() instead.
     */
    @Deprecated
    public static Pace ofMinutesPerKilometer(int minutes, int seconds) {
        return create(minutes, seconds).orElseThrow(IllegalArgumentException::new);
    }

    /**
     * @deprecated Use create() instead.
     */
    @Deprecated
    public static Pace ofSecondsPerKilometer(long seconds) {
        return create(seconds).orElseThrow(IllegalArgumentException::new);
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
