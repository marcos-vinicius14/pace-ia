package com.paceai.domain.exceptions;

import java.util.UUID;

/**
 * Exception thrown when an entity is not found.
 */
public class EntityNotFoundException extends DomainException {

    private final String entityType;
    private final String entityId;

    public EntityNotFoundException(String entityType, UUID id) {
        super(String.format("%s not found with id: %s", entityType, id));
        this.entityType = entityType;
        this.entityId = id.toString();
    }

    public EntityNotFoundException(String entityType, String id) {
        super(String.format("%s not found with id: %s", entityType, id));
        this.entityType = entityType;
        this.entityId = id;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getEntityId() {
        return entityId;
    }
}
