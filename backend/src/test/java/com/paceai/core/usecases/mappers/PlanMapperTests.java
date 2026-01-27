package com.paceai.core.usecases.mappers;

import java.time.LocalDate;
import java.util.UUID;

import com.paceai.core.domain.shared.Result;
import com.paceai.core.domain.training.plan.GoalDistance;
import com.paceai.core.domain.training.plan.TrainingPlan;
import com.paceai.core.usecases.dtos.CreatePlanRequest;
import com.paceai.core.usecases.dtos.PlanResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlanMapperTests {

    private final PlanMapper mapper = new PlanMapper();

    @Test
    @DisplayName("Should convert CreatePlanRequest to Domain Entity successfully")
    void shouldConvertToDomain() {
        // Arrange
        String athleteId = UUID.randomUUID().toString();
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate raceDate = startDate.plusWeeks(12);
        
        CreatePlanRequest request = new CreatePlanRequest(
                athleteId,
                "10k",
                startDate,
                raceDate,
                30.0
        );

        // Act
        Result<TrainingPlan> result = mapper.toDomain(request);

        // Assert
        assertThat(result.isSuccess()).isTrue();
        TrainingPlan plan = result.getValue();
        assertThat(plan.athleteId().value().toString()).isEqualTo(athleteId);
        assertThat(plan.goalDistance()).isEqualTo(GoalDistance.TEN_K);
        assertThat(plan.startDate()).isEqualTo(startDate);
        assertThat(plan.raceDate()).isEqualTo(raceDate);
        assertThat(plan.weeklyVolume().getValueInKilometers()).isEqualTo(30.0);
    }

    @Test
    @DisplayName("Should fail when CreatePlanRequest has invalid athlete ID")
    void shouldFailWithInvalidAthleteId() {
        // Arrange
        CreatePlanRequest request = new CreatePlanRequest(
                "invalid-uuid",
                "10k",
                LocalDate.now(),
                LocalDate.now().plusWeeks(12),
                30.0
        );

        // Act
        Result<TrainingPlan> result = mapper.toDomain(request);

        // Assert
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrorMessage()).contains("AthleteId inválido");
    }

    @Test
    @DisplayName("Should fail when dates are invalid (start date after race date)")
    void shouldFailWithInvalidDates() {
        // Arrange
        LocalDate startDate = LocalDate.now().plusWeeks(12);
        LocalDate raceDate = LocalDate.now().plusDays(1);
        
        CreatePlanRequest request = new CreatePlanRequest(
                UUID.randomUUID().toString(),
                "10k",
                startDate,
                raceDate,
                30.0
        );

        // Act
        Result<TrainingPlan> result = mapper.toDomain(request);

        // Assert
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrorMessage()).contains("Data de início deve ser anterior à data da prova");
    }

    @Test
    @DisplayName("Should convert Domain Entity to PlanResponse successfully")
    void shouldConvertToResponse() {
        // Arrange
        // Using the mapper to create a valid domain object first (or could mock/build manually)
        CreatePlanRequest request = new CreatePlanRequest(
                UUID.randomUUID().toString(),
                "MARATHON",
                LocalDate.now(),
                LocalDate.now().plusWeeks(16),
                50.0
        );
        TrainingPlan plan = mapper.toDomain(request).getValue();

        // Act
        PlanResponse response = mapper.toResponse(plan);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.planId()).isEqualTo(plan.id().value().toString());
        assertThat(response.athleteId()).isEqualTo(plan.athleteId().value().toString());
        assertThat(response.goalDistance()).isEqualTo("Marathon");
        assertThat(response.weeklyVolumeKm()).isEqualTo(50.0);
        assertThat(response.status()).isEqualTo("DRAFT");
    }
}
