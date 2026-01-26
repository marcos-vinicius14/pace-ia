package com.paceai.core.domain.shared;

import com.github.f4b6a3.uuid.UuidCreator;

import java.util.UUID;

public final class Identifiers {

    private Identifiers() {
        throw new AssertionError("Identifiers is a utility class and cannot be instantiated");
    }

    public static UUID newId() {
        return UuidCreator.getTimeOrderedEpoch();
    }
}
