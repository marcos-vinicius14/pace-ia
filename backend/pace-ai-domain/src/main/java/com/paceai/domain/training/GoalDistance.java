package com.paceai.domain.training;

/**
 * Goal Distance Enum with State Machine behavior.
 * <p>
 * Represents the target race distance for a training plan.
 * </p>
 */
public enum GoalDistance {
    FIVE_K("5K", 5.0),
    TEN_K("10K", 10.0),
    HALF_MARATHON("HM", 21.0975),
    MARATHON("Marathon", 42.195);

    private final String displayName;
    private final double distanceKm;

    GoalDistance(String displayName, double distanceKm) {
        this.displayName = displayName;
        this.distanceKm = distanceKm;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public int getTypicalWeeks() {
        return switch (this) {
            case FIVE_K -> 8;
            case TEN_K -> 10;
            case HALF_MARATHON -> 12;
            case MARATHON -> 16;
        };
    }
}
