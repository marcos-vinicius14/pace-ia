package com.paceai.infra.messaging.sqs;

import com.paceai.core.gateways.EventPublisher;
import org.springframework.stereotype.Component;

/**
 * AWS SQS implementation of the EventPublisher Port.
 * <p>
 * This adapter publishes domain events to AWS SQS queues.
 * </p>
 */
@Component
public class SqsEventPublisher implements EventPublisher {

    // TODO: Inject SQS template from Spring Cloud AWS

    @Override
    public void publish(DomainEvent event) {
        // TODO: Implement SQS message publishing
        // 1. Convert event to message
        // 2. Send to appropriate queue based on event type
    }
}
