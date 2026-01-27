package com.paceai.core.usecases.services;

import java.util.Optional;
import java.util.UUID;

import com.paceai.core.domain.shared.Result;
import com.paceai.core.domain.training.TrainingPlanId;
import com.paceai.core.domain.training.plan.TrainingPlan;
import com.paceai.core.gateways.TrainingPlanRepository;
import com.paceai.core.usecases.dtos.PlanDto;
import com.paceai.core.usecases.mappers.PlanMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPlanUseCaseTests {

    @Mock
    private TrainingPlanRepository planRepository;

    @Mock
    private PlanMapper planMapper;

    @InjectMocks
    private GetPlanUseCase useCase;

    @Test
    @DisplayName("Should return plan DTO when plan exists")
    void shouldReturnPlanWhenExists() {
        // Arrange
        String planIdStr = UUID.randomUUID().toString();
        // Create a real TrainingPlan instance instead of mocking final class
        TrainingPlan realPlan = TrainingPlan.create(
                com.paceai.core.domain.athlete.AthleteId.create(),
                com.paceai.core.domain.shared.Distance.ofKilometers(50.0),
                java.time.LocalDate.now(),
                com.paceai.core.domain.training.plan.GoalDistance.MARATHON,
                java.time.LocalDate.now().plusWeeks(16)
        );
        
        PlanDto realDto = new PlanDto(
            planIdStr,
            realPlan.athleteId().value().toString(),
            realPlan.goalDistance().getDisplayName(),
            realPlan.raceDate(),
            realPlan.status().name(),
            java.util.Collections.emptyList()
        );

        // Configure repository to return the real plan
        when(planRepository.findById(any(TrainingPlanId.class))).thenReturn(Optional.of(realPlan));
        
        // Configure mapper to accept any TrainingPlan (or the specific one) and return mockDto
        when(planMapper.toDto(any(TrainingPlan.class))).thenReturn(realDto);

        // Act
        Result<PlanDto> result = useCase.execute(planIdStr);

        // Assert
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue()).isEqualTo(realDto);
    }

    @Test
    @DisplayName("Should return failure when plan does not exist")
    void shouldReturnFailureWhenNotFound() {
        // Arrange
        String planIdStr = UUID.randomUUID().toString();
        when(planRepository.findById(any(TrainingPlanId.class))).thenReturn(Optional.empty());

        // Act
        Result<PlanDto> result = useCase.execute(planIdStr);

        // Assert
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrorMessage()).contains("Plan not found");
    }

    @Test
    @DisplayName("Should return failure for invalid UUID")
    void shouldReturnFailureForInvalidId() {
        // Act
        Result<PlanDto> result = useCase.execute("invalid-uuid");

        // Assert
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrorMessage()).contains("Invalid Plan ID format");
    }
}
