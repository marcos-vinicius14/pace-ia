package com.paceai.infra.web.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * SSE Stream Controller for real-time updates.
 */
@RestController
@RequestMapping("/api/v1/stream")
public class StreamController {

    @GetMapping(value = "/plan/{planId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PlanUpdateEvent> streamPlanUpdates(@PathVariable String planId) {
        // TODO: Implement SSE stream for plan generation updates
        return Flux.interval(Duration.ofSeconds(1))
                .take(10)
                .map(i -> new PlanUpdateEvent(planId, "PROCESSING", "Step " + i));
    }

    public record PlanUpdateEvent(String planId, String status, String message) {}
}
