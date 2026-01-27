package com.paceai.infra.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import com.paceai.core.domain.shared.Result;
import com.paceai.core.usecases.dtos.CreatePlanRequest;
import com.paceai.core.usecases.dtos.PlanDto;
import com.paceai.core.usecases.dtos.PlanResponse;
import com.paceai.core.usecases.services.GeneratePlanUseCase;
import com.paceai.core.usecases.services.GetPlanUseCase;

/**
 * REST Controller for Training Plan operations.
 * <p>
 * This is a Web Layer component that exposes REST endpoints for plan management.
 * It delegates business logic to Application Layer services.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/plans")
public class PlanController {

    private final GeneratePlanUseCase generatePlanUseCase;
    private final GetPlanUseCase getPlanUseCase;

    public PlanController(
            GeneratePlanUseCase generatePlanUseCase,
            GetPlanUseCase getPlanUseCase
    ) {
        this.generatePlanUseCase = generatePlanUseCase;
        this.getPlanUseCase = getPlanUseCase;
    }

    /**
     * Generates a new training plan.
     * Returns 201 Created with the created plan details.
     */
    @PostMapping
    public Mono<ResponseEntity<PlanResponse>> generatePlan(
            @RequestBody CreatePlanRequest request
    ) {
        Result<PlanResponse> result = generatePlanUseCase.execute(request);
        if (result.isFailure()) {
            return Mono.just(ResponseEntity.<PlanResponse>badRequest().build()); // Could add error message body
        }
        return Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(result.getValue()));
    }

    /**
     * Retrieves a plan by ID.
     */
    @GetMapping("/{planId}")
    public Mono<ResponseEntity<PlanDto>> getPlan(@PathVariable String planId) {
        Result<PlanDto> result = getPlanUseCase.execute(planId);
        if (result.isFailure()) {
            return Mono.just(ResponseEntity.notFound().build());
        }
        return Mono.just(ResponseEntity.ok(result.getValue()));
    }

    /**
     * Lists all plans for the authenticated athlete.
     */
    @GetMapping
    public Mono<ResponseEntity<Object>> listPlans() {
        // TODO: Implement list plans endpoint
        return Mono.just(ResponseEntity.ok().build());
    }

    // TODO: Implement get/list endpoints
}
