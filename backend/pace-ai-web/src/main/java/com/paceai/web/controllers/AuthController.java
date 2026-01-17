package com.paceai.web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Authentication Controller.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse>> login(@RequestBody LoginRequest request) {
        // TODO: Implement login endpoint
        return Mono.just(ResponseEntity.ok(new LoginResponse("user-id", "session-token")));
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<Void>> logout() {
        // TODO: Implement logout endpoint
        return Mono.just(ResponseEntity.noContent().build());
    }

    @GetMapping("/strava/callback")
    public Mono<ResponseEntity<Void>> stravaCallback(@RequestParam String code) {
        // TODO: Implement Strava OAuth callback
        return Mono.just(ResponseEntity.ok().build());
    }

    public record LoginRequest(String email, String password) {}
    public record LoginResponse(String userId, String sessionToken) {}
}
