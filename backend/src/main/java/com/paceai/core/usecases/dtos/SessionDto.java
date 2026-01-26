package com.paceai.core.usecases.dtos;

import java.time.LocalDate;

/**
 * Session DTO for API responses.
 */
public record SessionDto(
        String id,
        LocalDate scheduledDate,
        String type,
        String status,
        String details
) {}
