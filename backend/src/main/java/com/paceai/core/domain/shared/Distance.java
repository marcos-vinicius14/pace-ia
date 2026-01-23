package com.paceai.core.domain.shared;

import java.util.Objects;

/**
 * Distance Value Object.
 * <p>
 * Immutable value object representing a distance measurement.
 * </p>
 */
public final class Distance {

    private final double value;
    private final DistanceUnit unit;

    private Distance(double value, DistanceUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    public static Result<Distance> createKilometers(double km) {
        if (km < 0) {
            return Result.failure("A distância não pode ser negativa");
        }
        return Result.success(new Distance(km, DistanceUnit.KILOMETERS));
    }

    public static Result<Distance> createMiles(double miles) {
        if (miles < 0) {
            return Result.failure("A distância não pode ser negativa");
        }
        return Result.success(new Distance(miles, DistanceUnit.MILES));
    }

    public static Result<Distance> createMeters(double meters) {
        if (meters < 0) {
            return Result.failure("A distância não pode ser negativa");
        }
        return Result.success(new Distance(meters / 1000.0, DistanceUnit.KILOMETERS));
    }

    /**
     * @deprecated Use createKilometers() instead.
     */
    @Deprecated
    public static Distance ofKilometers(double km) {
        return createKilometers(km).orElseThrow(IllegalArgumentException::new);
    }

    /**
     * @deprecated Use createMiles() instead.
     */
    @Deprecated
    public static Distance ofMiles(double miles) {
        return createMiles(miles).orElseThrow(IllegalArgumentException::new);
    }

    /**
     * @deprecated Use createMeters() instead.
     */
    @Deprecated
    public static Distance ofMeters(double meters) {
        return createMeters(meters).orElseThrow(IllegalArgumentException::new);
    }

    public double getValueInKilometers() {
        return switch (unit) {
            case KILOMETERS -> value;
            case MILES -> value * 1.60934;
        };
    }

    public double getValueInMiles() {
        return switch (unit) {
            case KILOMETERS -> value / 1.60934;
            case MILES -> value;
        };
    }

    public double getValue() {
        return value;
    }

    public DistanceUnit getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance = (Distance) o;
        return Double.compare(getValueInKilometers(), distance.getValueInKilometers()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValueInKilometers());
    }

    public Distance add(Distance other) {
        double totalKm = this.getValueInKilometers() + other.getValueInKilometers();
        return new Distance(totalKm, DistanceUnit.KILOMETERS);
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", value, unit.getAbbreviation());
    }

    public enum DistanceUnit {
        KILOMETERS("km"),
        MILES("mi");

        private final String abbreviation;

        DistanceUnit(String abbreviation) {
            this.abbreviation = abbreviation;
        }

        public String getAbbreviation() {
            return abbreviation;
        }
    }
}
