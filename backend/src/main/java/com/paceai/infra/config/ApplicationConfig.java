package com.paceai.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paceai.core.usecases.services.GeneratePlanService;
import com.paceai.core.gateways.AIGatewayPort;
import com.paceai.core.gateways.EventPublisher;
import com.paceai.core.gateways.TrainingPlanRepository;

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
