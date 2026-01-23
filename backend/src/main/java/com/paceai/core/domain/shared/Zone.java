package com.paceai.core.domain.shared;

import java.util.Objects;

/**
 * Heart Rate Zone Value Object.
 * <p>
 * Immutable value object representing a heart rate training zone.
 * </p>
 */
public final class Zone {

    private final int number;
    private final String name;
    private final int minBpm;
    private final int maxBpm;

    private Zone(int number, String name, int minBpm, int maxBpm) {
        if (minBpm >= maxBpm) {
            throw new IllegalArgumentException("Min BPM must be less than Max BPM");
        }
        this.number = number;
        this.name = Objects.requireNonNull(name);
        this.minBpm = minBpm;
        this.maxBpm = maxBpm;
    }

    public static Zone of(int number, String name, int minBpm, int maxBpm) {
        return new Zone(number, name, minBpm, maxBpm);
    }

    public boolean containsBpm(int bpm) {
        return bpm >= minBpm && bpm <= maxBpm;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public int getMinBpm() {
        return minBpm;
    }

    public int getMaxBpm() {
        return maxBpm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zone zone = (Zone) o;
        return number == zone.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return String.format("Zone %d (%s): %d-%d bpm", number, name, minBpm, maxBpm);
    }
}
