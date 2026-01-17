package com.paceai.infrastructure.persistence.postgres.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "strava_activities")
public class StravaActivityEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "raw_data_jsonb", columnDefinition = "jsonb", nullable = false)
    private String rawDataJsonb;

    @Column(name = "athlete_id", insertable = false, updatable = false)
    private UUID athleteId;

    protected StravaActivityEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRawDataJsonb() {
        return rawDataJsonb;
    }

    public void setRawDataJsonb(String rawDataJsonb) {
        this.rawDataJsonb = rawDataJsonb;
    }

    public UUID getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(UUID athleteId) {
        this.athleteId = athleteId;
    }
}
