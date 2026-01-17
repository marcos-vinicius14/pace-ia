package com.paceai.domain.shared;

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
        if (value < 0) {
            throw new IllegalArgumentException("A distância não pode ser negativa");
        }
        this.value = value;
        this.unit = Objects.requireNonNull(unit, "A unidade não pode estar vazia.");
    }

    public static Distance ofKilometers(double km) {
        return new Distance(km, DistanceUnit.KILOMETERS);
    }

    public static Distance ofMiles(double miles) {
        return new Distance(miles, DistanceUnit.MILES);
    }

    public static Distance ofMeters(double meters) {
        return new Distance(meters / 1000.0, DistanceUnit.KILOMETERS);
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
