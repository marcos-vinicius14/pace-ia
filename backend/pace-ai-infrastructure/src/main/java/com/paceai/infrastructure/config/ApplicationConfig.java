package com.paceai.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paceai.application.services.GeneratePlanService;
import com.paceai.domain.ports.AIGatewayPort;
import com.paceai.domain.ports.EventPublisher;
import com.paceai.domain.ports.TrainingPlanRepository;

/**
 * Application Beans Configuration.
 * <p>
 * Wires up the application services with their port implementations.
 * </p>
 */
@Configuration
public class ApplicationConfig {

    @Bean
    public GeneratePlanService generatePlanService(
            TrainingPlanRepository planRepository,
            AIGatewayPort aiGateway,
            EventPublisher eventPublisher
    ) {
        return new GeneratePlanService(planRepository, aiGateway, eventPublisher);
    }
}
