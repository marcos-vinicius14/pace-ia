package com.paceai.infrastructure.external.strava;

import org.springframework.stereotype.Component;

/**
 * Strava API Client.
 * <p>
 * This adapter communicates with the Strava API for:
 * - OAuth2 authentication
 * - Activity synchronization
 * - Webhook handling
 * </p>
 */
@Component
public class StravaClient {

    // TODO: Inject WebClient for HTTP calls

    /**
     * Fetches athlete activities from Strava.
     */
    public void fetchActivities(String accessToken) {
        // TODO: Implement Strava API call to get activities
    }

    /**
     * Exchanges authorization code for tokens.
     */
    public void exchangeToken(String authorizationCode) {
        // TODO: Implement OAuth2 token exchange
    }

    /**
     * Refreshes an expired access token.
     */
    public void refreshToken(String refreshToken) {
        // TODO: Implement token refresh
    }
}
