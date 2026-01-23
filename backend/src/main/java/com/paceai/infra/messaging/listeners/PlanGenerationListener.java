package com.paceai.infra.messaging.listeners;

import org.springframework.stereotype.Component;

/**
 * SQS Message Listener (Consumer/Worker).
 * <p>
 * Consumes messages from SQS queue for async plan generation.
 * </p>
 */
@Component
public class PlanGenerationListener {

    // TODO: Inject GeneratePlanService

    // @SqsListener annotation will be added here
    public void onMessage(String message) {
        // TODO: Implement message handling
        // 1. Deserialize message
        // 2. Call GeneratePlanService
        // 3. Handle errors and DLQ
    }
}
