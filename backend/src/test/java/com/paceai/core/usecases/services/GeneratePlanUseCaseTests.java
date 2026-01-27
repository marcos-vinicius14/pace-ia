package com.paceai.core.usecases.services;

import java.time.LocalDate;
import java.util.UUID;

import com.paceai.core.domain.shared.Result;
import com.paceai.core.domain.training.plan.TrainingPlan;
import com.paceai.core.gateways.TrainingPlanRepository;
import com.paceai.core.usecases.dtos.CreatePlanRequest;
import com.paceai.core.usecases.dtos.PlanResponse;
import com.paceai.core.usecases.mappers.PlanMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeneratePlanUseCaseTests {

    @Mock
    private TrainingPlanRepository planRepository;

    @Spy
    private PlanMapper planMapper; // Using Spy to use real mapper logic or Mock if preferred

    @InjectMocks
    private GeneratePlanUseCase useCase;

    @Test
    @DisplayName("Should execute use case successfully")
    void shouldExecuteSuccessfully() {
        // Arrange
        CreatePlanRequest request = new CreatePlanRequest(
                UUID.randomUUID().toString(),
                "5k",
                LocalDate.now(),
                LocalDate.now().plusWeeks(8),
                20.0
        );

        // Mock repository save behavior
        when(planRepository.save(any(TrainingPlan.class)))
                .thenAnswer(invocation -> Result.success(invocation.getArgument(0)));

        // Act
        Result<PlanResponse> result = useCase.execute(request);

        // Assert
        assertThat(result.isSuccess()).isTrue();
        PlanResponse response = result.getValue();
        assertThat(response).isNotNull();
        assertThat(response.planId()).isNotNull();
        assertThat(response.status()).isEqualTo("DRAFT");
        
        verify(planRepository).save(any(TrainingPlan.class));
    }

    @Test
    @DisplayName("Should return failure when validation fails")
    void shouldReturnFailureOnValidation() {
        // Arrange
        CreatePlanRequest invalidRequest = new CreatePlanRequest(
                null, // Missing Athlete ID
                "5k",
                LocalDate.now(),
                LocalDate.now().plusWeeks(8),
                20.0
        );

        // Act
        Result<PlanResponse> result = useCase.execute(invalidRequest);

        // Assert
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrorMessage()).contains("AthleteId é obrigatório");
    }

    @Test
    @DisplayName("Should return failure when repository save fails")
    void shouldReturnFailureOnRepoError() {
        // Arrange
        CreatePlanRequest request = new CreatePlanRequest(
                UUID.randomUUID().toString(),
                "5k",
                LocalDate.now(),
                LocalDate.now().plusWeeks(8),
                20.0
        );

        when(planRepository.save(any(TrainingPlan.class)))
                .thenReturn(Result.failure("Database error"));

        // Act
        Result<PlanResponse> result = useCase.execute(request);

        // Assert
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrorMessage()).isEqualTo("Database error");
    }
}
