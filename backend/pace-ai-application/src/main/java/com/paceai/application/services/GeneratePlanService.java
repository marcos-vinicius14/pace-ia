package com.paceai.application.services;

import com.paceai.domain.ports.AIGatewayPort;
import com.paceai.domain.ports.EventPublisher;
import com.paceai.domain.ports.TrainingPlanRepository;
import com.paceai.domain.shared.Result;
import com.paceai.domain.training.plan.TrainingPlan;

/**
 * Generate Plan Use Case (Application Service).
 * <p>
 * This is an Application Layer service that orchestrates the plan generation flow.
 * It uses Ports (interfaces) to interact with external systems, following
 * the Dependency Inversion Principle.
 * </p>
 */
public class GeneratePlanService {

    private final TrainingPlanRepository planRepository;
    private final AIGatewayPort aiGateway;
    private final EventPublisher eventPublisher;

    public GeneratePlanService(
            TrainingPlanRepository planRepository,
            AIGatewayPort aiGateway,
            EventPublisher eventPublisher
    ) {
        this.planRepository = planRepository;
        this.aiGateway = aiGateway;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Generates a new training plan for an athlete.
     *
     * @param request the plan generation request
     * @return the generated plan result
     */
    public Result<TrainingPlan> execute(GeneratePlanRequest request) {
        // TODO: Implement TDD - RED phase first
        // 1. Validate request
        // 2. Call AI Gateway to generate plan
        // 3. Validate generated plan (15% progression rule)
        // 4. Save plan to repository
        // 5. Publish domain event
        // 6. Return saved plan
        return Result.failure("Not implemented");
    }

    /**
     * Request DTO for generating a plan.
     */
    public record GeneratePlanRequest(
            String athleteId,
            String goalDistance,
            String raceDate,
            int currentLevel,
            int availableDaysPerWeek
    ) {}

    /**
     * Response DTO for the generated plan.
     */
    public record GeneratePlanResponse(
            String planId,
            String status,
            String message
    ) {}
}
