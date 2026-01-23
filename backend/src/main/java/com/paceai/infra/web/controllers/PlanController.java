package com.paceai.infra.web.controllers;

import com.paceai.core.usecases.services.GeneratePlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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

    private final GeneratePlanService generatePlanService;

    public PlanController(GeneratePlanService generatePlanService) {
        this.generatePlanService = generatePlanService;
    }

    /**
     * Generates a new training plan.
     * Returns 202 Accepted with a plan ID (async processing via SQS).
     */
    @PostMapping
    public Mono<ResponseEntity<GeneratePlanResponse>> generatePlan(
            @RequestBody GeneratePlanRequest request
    ) {
        // TODO: Implement reactive endpoint
        // 1. Validate request
        // 2. Send message to SQS queue
        // 3. Return 202 Accepted with plan ID
        return Mono.just(ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new GeneratePlanResponse("plan-id-placeholder", "PROCESSING")
        ));
    }

    /**
     * Retrieves a plan by ID.
     */
    @GetMapping("/{planId}")
    public Mono<ResponseEntity<Object>> getPlan(@PathVariable String planId) {
        // TODO: Implement get plan endpoint
        return Mono.just(ResponseEntity.ok().build());
    }

    /**
     * Lists all plans for the authenticated athlete.
     */
    @GetMapping
    public Mono<ResponseEntity<Object>> listPlans() {
        // TODO: Implement list plans endpoint
        return Mono.just(ResponseEntity.ok().build());
    }

    // Request/Response DTOs
    public record GeneratePlanRequest(
            String goalDistance,
            String raceDate,
            int currentLevel,
            int availableDaysPerWeek
    ) {}

    public record GeneratePlanResponse(
            String planId,
            String status
    ) {}
}
