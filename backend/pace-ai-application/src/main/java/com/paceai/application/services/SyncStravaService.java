package com.paceai.application.services;

/**
 * Sync Strava Use Case (Application Service).
 * <p>
 * Orchestrates synchronization of Strava activities with training sessions.
 * </p>
 */
public class SyncStravaService {

    // TODO: Inject required ports

    public void execute(SyncStravaRequest request) {
        // TODO: Implement Strava sync logic
        // 1. Fetch activities from Strava
        // 2. Match activities with training sessions
        // 3. Update session status
        // 4. Publish sync completed event
    }

    public record SyncStravaRequest(
            String athleteId,
            String accessToken
    ) {}
}
