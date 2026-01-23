package com.paceai.infra.ai.springai;

import com.paceai.core.gateways.AIGatewayPort;
import com.paceai.core.domain.training.plan.TrainingPlan;
import org.springframework.stereotype.Component;

/**
 * Spring AI (OpenAI) implementation of the AIGatewayPort.
 * <p>
 * This adapter uses Spring AI to interact with OpenAI/Anthropic models
 * for generating training plans.
 * </p>
 */
@Component
public class SpringAIGateway implements AIGatewayPort {

    // TODO: Inject ChatClient from Spring AI

    @Override
    public TrainingPlan generatePlan(PlanGenerationRequest request) {
        // TODO: Implement AI plan generation
        // 1. Build prompt with athlete data and race goal
        // 2. Call AI model via ChatClient
        // 3. Parse response into Plan entity
        // 4. Validate plan against domain rules
        return null;
    }
}
