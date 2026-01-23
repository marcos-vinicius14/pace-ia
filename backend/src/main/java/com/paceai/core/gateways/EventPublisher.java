package com.paceai.core.gateways;

/**
 * Event Publisher Port (Interface).
 * <p>
 * This is a Port that defines the contract for publishing domain events
 * to a message queue (SQS, Kafka, etc.).
 * </p>
 */
public interface EventPublisher {

    /**
     * Publishes an event to the message queue.
     *
     * @param event the event to publish
     */
    void publish(DomainEvent event);

    /**
     * Base interface for domain events.
     */
    interface DomainEvent {
        String eventType();
        String aggregateId();
    }
}
