package com.paceai.infra.persistence.postgres.adapters;

import com.paceai.core.domain.athlete.AthleteId;
import com.paceai.core.domain.shared.Distance;
import com.paceai.core.domain.shared.Result;
import com.paceai.core.domain.shared.Identifiers;
import com.paceai.core.domain.training.SessionId;
import com.paceai.core.domain.training.TrainingPlanId;
import com.paceai.core.domain.training.plan.GoalDistance;
import com.paceai.core.domain.training.plan.PlanStatus;
import com.paceai.core.domain.training.plan.TrainingPlan;
import com.paceai.core.domain.training.session.Session;
import com.paceai.core.domain.training.session.SessionStatus;
import com.paceai.core.domain.training.session.SessionType;
import com.paceai.core.domain.training.session.Sessions;
import com.paceai.infra.persistence.postgres.entity.AthleteEntity;
import com.paceai.infra.persistence.postgres.mappers.SessionJpaMapper;
import com.paceai.infra.persistence.postgres.mappers.TrainingPlanJpaMapper;
import com.paceai.infra.persistence.postgres.repositories.JpaAthleteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EntityScan("com.paceai.infra.persistence.postgres.entity")
@EnableJpaRepositories(basePackages = "com.paceai.infra.persistence.postgres.repositories")
@ImportAutoConfiguration(FlywayAutoConfiguration.class)
@Import({PostgresTrainingPlanRepository.class, TrainingPlanJpaMapper.class, SessionJpaMapper.class})
class PostgresTrainingPlanRepositoryIntegrationTests {

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("paceai")
            .withUsername("paceai")
            .withPassword("paceai");

    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.flyway.enabled", () -> true);
        registry.add("spring.flyway.init-sqls", () -> "CREATE EXTENSION IF NOT EXISTS pgcrypto;");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
    }

    @Autowired
    private PostgresTrainingPlanRepository trainingPlanRepository;

    @Autowired
    private JpaAthleteRepository athleteRepository;

    @Test
    void shouldSaveAndLoadPlanWithSessions() {
        AthleteEntity athlete = new AthleteEntity();
        athlete.setId(Identifiers.newId());
        athlete.setStravaId(12345L);
        athlete.setStravaAccessToken("access-token");
        athlete.setStravaRefreshToken("refresh-token");
        athleteRepository.save(athlete);

        AthleteId athleteId = AthleteId.of(athlete.getId());
        TrainingPlanId planId = TrainingPlanId.create();
        Distance weeklyVolume = Distance.createKilometers(42.0).getValue();
        LocalDate startDate = LocalDate.now();
        LocalDate raceDate = startDate.plusWeeks(8);

        Session session = Session.builder()
                .id(SessionId.create())
                .planId(planId)
                .scheduledDate(startDate.plusDays(1))
                .type(SessionType.EASY)
                .status(SessionStatus.PENDING)
                .details("{\"note\":\"easy run\"}")
                .build();

        Sessions sessions = Sessions.of(List.of(session));

        TrainingPlan plan = TrainingPlan.reconstitute(
                planId,
                athleteId,
                weeklyVolume,
                startDate,
                GoalDistance.FIVE_K,
                raceDate,
                PlanStatus.DRAFT,
                sessions,
                "v1"
        );

        Result<TrainingPlan> savedResult = trainingPlanRepository.save(plan);
        assertThat(savedResult.isSuccess()).isTrue();

        Optional<TrainingPlan> found = trainingPlanRepository.findById(planId);
        assertThat(found).isPresent();
        TrainingPlan reloaded = found.get();

        assertThat(reloaded.athleteId()).isEqualTo(athleteId);
        assertThat(reloaded.goalDistance()).isEqualTo(GoalDistance.FIVE_K);
        assertThat(reloaded.sessions().count()).isEqualTo(1);
        Session reloadedSession = reloaded.sessions().asList().get(0);
        assertThat(reloadedSession.type()).isEqualTo(SessionType.EASY);
        assertThat(reloadedSession.status()).isEqualTo(SessionStatus.PENDING);
        assertThat(reloadedSession.details()).isEqualTo("{\"note\":\"easy run\"}");
    }
}
