package com.paceai.core.domain.training;

import com.paceai.core.domain.shared.Identifiers;
import java.util.UUID;
import java.util.Objects;

public record TrainingPlanId(UUID value) {
    public TrainingPlanId {
        Objects.requireNonNull(value, "TrainingPlanId cannot be null");
    }

    public static TrainingPlanId create() {
        return new TrainingPlanId(Identifiers.newId());
    }
    
    public static TrainingPlanId of(UUID value) {
        return new TrainingPlanId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
