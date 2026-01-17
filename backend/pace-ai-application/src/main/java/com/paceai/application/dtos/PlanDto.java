package com.paceai.application.dtos;

import java.time.LocalDate;
import java.util.List;

/**
 * Plan DTO for API responses.
 */
public record PlanDto(
        String id,
        String athleteId,
        String goalDistance,
        LocalDate raceDate,
        String status,
        List<SessionDto> sessions
) {}
