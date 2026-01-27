package com.paceai.infra.web.controllers;

import com.paceai.core.domain.athlete.AthleteId;
import com.paceai.core.domain.training.plan.TrainingPlan;
import com.paceai.core.gateways.TrainingPlanRepository;
import com.paceai.core.usecases.dtos.CreatePlanRequest;
import com.paceai.core.usecases.dtos.PlanResponse;
import com.paceai.core.usecases.mappers.PlanMapper;
import com.paceai.core.usecases.services.GeneratePlanUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class PlanControllerIntegrationTests {

    private WebTestClient webTestClient;
    private InMemoryTrainingPlanRepository planRepository;

    @BeforeEach
    void setUp() {
        planRepository = new InMemoryTrainingPlanRepository();
        PlanMapper planMapper = new PlanMapper();
        GeneratePlanUseCase generateUseCase = new GeneratePlanUseCase(planRepository, planMapper);
        com.paceai.core.usecases.services.GetPlanUseCase getUseCase = new com.paceai.core.usecases.services.GetPlanUseCase(planRepository, planMapper);
        PlanController controller = new PlanController(generateUseCase, getUseCase);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void shouldCreatePlan() {
        String athleteId = UUID.randomUUID().toString();
        LocalDate startDate = LocalDate.now();
        LocalDate raceDate = startDate.plusWeeks(8);

        CreatePlanRequest request = new CreatePlanRequest(
                athleteId,
                "5K",
                startDate,
                raceDate,
                45.0
        );

        webTestClient.post()
                .uri("/api/v1/plans")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PlanResponse.class)
                .value(response -> {
                    TrainingPlan savedPlan = planRepository.lastSaved();
                    assertThat(savedPlan).isNotNull();
                    assertThat(response.planId()).isEqualTo(savedPlan.id().value().toString());
                    assertThat(response.athleteId()).isEqualTo(athleteId);
                    assertThat(response.goalDistance()).isEqualTo("5K");
                    assertThat(response.status()).isEqualTo("DRAFT");
                    assertThat(response.weeklyVolumeKm()).isEqualTo(45.0);
                });
    }

    static class InMemoryTrainingPlanRepository implements TrainingPlanRepository {
        private final AtomicReference<TrainingPlan> lastSaved = new AtomicReference<>();

        @Override
        public com.paceai.core.domain.shared.Result<TrainingPlan> save(TrainingPlan plan) {
            lastSaved.set(plan);
            return com.paceai.core.domain.shared.Result.success(plan);
        }

        @Override
        public Optional<TrainingPlan> findById(com.paceai.core.domain.training.TrainingPlanId id) {
            return Optional.ofNullable(lastSaved.get()).filter(plan -> plan.id().equals(id));
        }

        @Override
        public List<TrainingPlan> findByAthleteId(AthleteId athleteId) {
            TrainingPlan plan = lastSaved.get();
            if (plan == null || !plan.athleteId().equals(athleteId)) {
                return List.of();
            }
            return List.of(plan);
        }

        @Override
        public void deleteById(com.paceai.core.domain.training.TrainingPlanId id) {
            TrainingPlan plan = lastSaved.get();
            if (plan != null && plan.id().equals(id)) {
                lastSaved.set(null);
            }
        }

        @Override
        public boolean existsById(com.paceai.core.domain.training.TrainingPlanId id) {
            TrainingPlan plan = lastSaved.get();
            return plan != null && plan.id().equals(id);
        }

        TrainingPlan lastSaved() {
            return lastSaved.get();
        }
    }
}
