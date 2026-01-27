package com.paceai.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paceai.core.gateways.TrainingPlanRepository;
import com.paceai.core.usecases.mappers.PlanMapper;
import com.paceai.core.usecases.services.GeneratePlanUseCase;

/**
 * Application Beans Configuration.
 * <p>
 * Wires up the application services with their port implementations.
 * </p>
 */
@Configuration
public class ApplicationConfig {

    @Bean
    public PlanMapper planMapper() {
        return new PlanMapper();
    }

    @Bean
    public GeneratePlanUseCase generatePlanUseCase(
            TrainingPlanRepository planRepository,
            PlanMapper planMapper
    ) {
        return new GeneratePlanUseCase(planRepository, planMapper);
    }

    @Bean
    public com.paceai.core.usecases.services.GetPlanUseCase getPlanUseCase(
            TrainingPlanRepository planRepository,
            PlanMapper planMapper
    ) {
        return new com.paceai.core.usecases.services.GetPlanUseCase(planRepository, planMapper);
    }
}
