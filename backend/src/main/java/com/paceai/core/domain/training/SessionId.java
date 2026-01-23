package com.paceai.core.domain.training;

import com.paceai.core.domain.shared.Identifiers;
import java.util.UUID;
import java.util.Objects;

public record SessionId(UUID value) {
    public SessionId {
        Objects.requireNonNull(value, "SessionId cannot be null");
    }

    public static SessionId create() {
        return new SessionId(Identifiers.newId());
    }

    public static SessionId of(UUID value) {
        return new SessionId(value);
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
}
