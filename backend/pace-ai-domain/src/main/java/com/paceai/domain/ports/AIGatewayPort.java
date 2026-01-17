package com.paceai.domain.ports;

import com.paceai.domain.training.Plan;

/**
 * AI Gateway Port (Interface).
 * <p>
 * This is a Port that defines the contract for interacting with AI services
 * (OpenAI, Anthropic) to generate training plans.
 * </p>
 */
public interface AIGatewayPort {

    /**
     * Generates a training plan using AI.
     *
     * @param request the plan generation request
     * @return the generated plan
     */
    Plan generatePlan(PlanGenerationRequest request);

    /**
     * Request object for plan generation.
     */
    record PlanGenerationRequest(
            String goalDistance,
            String raceDate,
            int currentLevel,
            int availableDaysPerWeek,
            String athleteProfile
    ) {}
}
