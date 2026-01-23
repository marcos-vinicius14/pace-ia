-- Migration V2: Update schema with new fields for TrainingPlan and Athlete

-- Add new columns to training_plans table
ALTER TABLE training_plans
ADD COLUMN weekly_volume_km DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
ADD COLUMN start_date DATE NOT NULL DEFAULT CURRENT_DATE;

-- Add new columns to athletes table
ALTER TABLE athletes
ADD COLUMN resting_heart_rate INTEGER CHECK (resting_heart_rate > 0),
ADD COLUMN weekly_mileage INTEGER CHECK (weekly_mileage >= 0);

-- Create index on start_date for better query performance
CREATE INDEX idx_training_plans_start_date ON training_plans(start_date);
