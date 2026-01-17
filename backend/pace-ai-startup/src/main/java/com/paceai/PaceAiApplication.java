package com.paceai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Pace AI Application Entry Point.
 * <p>
 * This is the main class that bootstraps the Spring Boot application.
 * It unifies all modules and starts the application context.
 * </p>
 */
@SpringBootApplication
public class PaceAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaceAiApplication.class, args);
    }
}
