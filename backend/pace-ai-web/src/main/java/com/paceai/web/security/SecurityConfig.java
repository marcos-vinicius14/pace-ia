package com.paceai.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Security Configuration for the Web Layer.
 * <p>
 * Configures Spring Security for the reactive WebFlux application.
 * </p>
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        // TODO: Implement full security configuration
        // - CSRF protection for SPA
        // - CORS configuration
        // - Session management with Redis
        // - OAuth2 login with Strava
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // TODO: Enable with proper SPA config
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/actuator/health").permitAll()
                        .pathMatchers("/api/v1/auth/**").permitAll()
                        .anyExchange().authenticated()
                )
                .build();
    }
}
