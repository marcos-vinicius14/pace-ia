package com.paceai.core.usecases.services;

import com.paceai.core.domain.shared.Result;
import com.paceai.core.domain.training.plan.TrainingPlan;
import com.paceai.core.gateways.TrainingPlanRepository;
import com.paceai.core.usecases.dtos.CreatePlanRequest;
import com.paceai.core.usecases.dtos.PlanResponse;
import com.paceai.core.usecases.mappers.PlanMapper;

/**
 * Generate Plan Use Case.
 * <p>
 * Orchestrates training plan creation and persistence.
 * </p>
 */
public class GeneratePlanUseCase {

    private final TrainingPlanRepository planRepository;
    private final PlanMapper planMapper;

    public GeneratePlanUseCase(
            TrainingPlanRepository planRepository,
            PlanMapper planMapper
    ) {
        this.planRepository = planRepository;
        this.planMapper = planMapper;
    }

    public Result<PlanResponse> execute(CreatePlanRequest request) {
        Result<TrainingPlan> planResult = planMapper.toDomain(request);
        if (planResult.isFailure()) {
            return Result.failure(planResult.getErrorMessage());
        }

        Result<TrainingPlan> savedResult = planRepository.save(planResult.getValue());
        if (savedResult.isFailure()) {
            return Result.failure(savedResult.getErrorMessage());
        }

        return Result.success(planMapper.toResponse(savedResult.getValue()));
    }
}
