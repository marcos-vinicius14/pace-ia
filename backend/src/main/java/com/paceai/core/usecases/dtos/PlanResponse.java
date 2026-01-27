package com.paceai.core.usecases.dtos;

import java.time.LocalDate;

/**
 * Response DTO for created training plans.
 */
public record PlanResponse(
        String planId,
        String athleteId,
        String goalDistance,
        LocalDate startDate,
        LocalDate raceDate,
        String status,
        double weeklyVolumeKm
) {}
