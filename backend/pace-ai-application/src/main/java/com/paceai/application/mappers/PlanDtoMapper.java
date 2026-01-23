package com.paceai.application.mappers;

import com.paceai.application.dtos.PlanDto;
import com.paceai.application.dtos.SessionDto;
import com.paceai.domain.training.plan.TrainingPlan;
import com.paceai.domain.training.session.Session;

import java.util.List;

/**
 * Mapper between Domain Entities and DTOs.
 */
public class PlanDtoMapper {

    public PlanDto toDto(TrainingPlan plan) {
        // TODO: Implement domain to DTO conversion
        return null;
    }

    public List<SessionDto> toSessionDtos(List<Session> sessions) {
        // TODO: Implement session list conversion
        return List.of();
    }

    public SessionDto toDto(Session session) {
        // TODO: Implement session to DTO conversion
        return null;
    }
}
