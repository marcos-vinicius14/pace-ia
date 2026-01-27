package com.paceai.core.usecases.services;

import com.paceai.core.domain.shared.Result;
import com.paceai.core.domain.training.TrainingPlanId;
import com.paceai.core.domain.training.plan.TrainingPlan;
import com.paceai.core.gateways.TrainingPlanRepository;
import com.paceai.core.usecases.dtos.PlanDto;
import com.paceai.core.usecases.mappers.PlanMapper;

import java.util.Optional;
import java.util.UUID;

/**
 * Get Plan Use Case.
 * <p>
 * Retrieves training plan details.
 * </p>
 */
public class GetPlanUseCase {

    private final TrainingPlanRepository planRepository;
    private final PlanMapper planMapper;

    public GetPlanUseCase(
            TrainingPlanRepository planRepository,
            PlanMapper planMapper
    ) {
        this.planRepository = planRepository;
        this.planMapper = planMapper;
    }

    public Result<PlanDto> execute(String planId) {
        if (planId == null || planId.isBlank()) {
            return Result.failure("Plan ID is required");
        }

        UUID uuid;
        try {
            uuid = UUID.fromString(planId);
        } catch (IllegalArgumentException e) {
            return Result.failure("Invalid Plan ID format");
        }

        Optional<TrainingPlan> planOpt = planRepository.findById(TrainingPlanId.of(uuid));
        
        return planOpt
                .map(plan -> Result.success(planMapper.toDto(plan)))
                .orElseGet(() -> Result.failure("Plan not found: " + planId));
    }
}
