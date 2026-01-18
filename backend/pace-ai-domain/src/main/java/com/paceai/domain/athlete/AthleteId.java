package com.paceai.domain.athlete;

import com.paceai.domain.shared.Identifiers;
import java.util.UUID;
import java.util.Objects;

public record AthleteId(UUID value) {
    public AthleteId {
        Objects.requireNonNull(value, "AthleteId cannot be null");
    }

    public static AthleteId create() {
        return new AthleteId(Identifiers.newId());
    }
    
    public static AthleteId of(UUID value) {
        return new AthleteId(value);
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
}
