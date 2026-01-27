package com.paceai.core.usecases.dtos;

import java.time.LocalDate;

/**
 * Request DTO for creating a training plan.
 */
public record CreatePlanRequest(
        String athleteId,
        String goalDistance,
        LocalDate startDate,
        LocalDate raceDate,
        double weeklyVolumeKm
) {}
