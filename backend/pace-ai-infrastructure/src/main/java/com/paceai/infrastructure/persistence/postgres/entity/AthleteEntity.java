package com.paceai.infrastructure.persistence.postgres.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "athletes")
public class AthleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "strava_id", unique = true, nullable = false)
    private Long stravaId;

    @Column(name = "strava_access_token", nullable = false)
    private String stravaAccessToken;

    @Column(name = "vdot_score", precision = 5, scale = 2)
    private Double vdotScore;

    @Column(name = "max_heart_rate")
    private Integer maxHeartRate;

    @Column(name = "email")
    private String email;

    @Column(name = "strava_refresh_token")
    private String stravaRefreshToken;

    public AthleteEntity() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getStravaId() {
        return stravaId;
    }

    public void setStravaId(Long stravaId) {
        this.stravaId = stravaId;
    }

    public String getStravaAccessToken() {
        return stravaAccessToken;
    }

    public void setStravaAccessToken(String stravaAccessToken) {
        this.stravaAccessToken = stravaAccessToken;
    }

    public Double getVdotScore() {
        return vdotScore;
    }

    public void setVdotScore(Double vdotScore) {
        this.vdotScore = vdotScore;
    }

    public Integer getMaxHeartRate() {
        return maxHeartRate;
    }

    public void setMaxHeartRate(Integer maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStravaRefreshToken() {
        return stravaRefreshToken;
    }

    public void setStravaRefreshToken(String stravaRefreshToken) {
        this.stravaRefreshToken = stravaRefreshToken;
    }
}
