-- Migration V1: Create initial schema for Pace AI database

-- Create athletes table
CREATE TABLE athletes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    strava_id BIGINT UNIQUE NOT NULL,
    strava_access_token TEXT NOT NULL,
    vdot_score DECIMAL(5, 2),
    max_heart_rate INTEGER,
    email VARCHAR(255) UNIQUE,
    strava_refresh_token TEXT
);

-- Create training_plans table
CREATE TABLE training_plans (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    athlete_id UUID NOT NULL REFERENCES athletes(id) ON DELETE CASCADE,
    goal_distance VARCHAR(20) NOT NULL CHECK (goal_distance IN ('FIVE_K', 'TEN_K', 'HALF_MARATHON', 'MARATHON')),
    race_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT' CHECK (status IN ('DRAFT', 'ACTIVE', 'COMPLETED', 'CANCELLED')),
    ai_model_version VARCHAR(50)
);

-- Create training_sessions table
CREATE TABLE training_sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    plan_id UUID NOT NULL REFERENCES training_plans(id) ON DELETE CASCADE,
    scheduled_date DATE NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('REST', 'EASY', 'LONG_RUN', 'TEMPO', 'INTERVAL', 'FARTLEK', 'RECOVERY', 'RACE')),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'COMPLETED', 'MISSED', 'SKIPPED')),
    details_jsonb JSONB,
    strava_activity_id BIGINT
);

-- Create strava_activities table for audit
CREATE TABLE strava_activities (
    id BIGINT PRIMARY KEY,
    raw_data_jsonb JSONB NOT NULL,
    athlete_id UUID REFERENCES athletes(id) ON DELETE SET NULL
);

-- Create indexes for better query performance
CREATE INDEX idx_athletes_strava_id ON athletes(strava_id);
CREATE INDEX idx_athletes_email ON athletes(email);
CREATE INDEX idx_training_plans_athlete_id ON training_plans(athlete_id);
CREATE INDEX idx_training_plans_status ON training_plans(status);
CREATE INDEX idx_training_sessions_plan_id ON training_sessions(plan_id);
CREATE INDEX idx_training_sessions_scheduled_date ON training_sessions(scheduled_date);
CREATE INDEX idx_training_sessions_plan_date ON training_sessions(plan_id, scheduled_date);
CREATE INDEX idx_strava_activities_athlete_id ON strava_activities(athlete_id);
