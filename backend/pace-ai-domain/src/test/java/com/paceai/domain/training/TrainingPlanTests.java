package com.paceai.domain.training;

import com.paceai.domain.athlete.AthleteId;
import com.paceai.domain.exceptions.ExcessiveLoadDomainException;
import com.paceai.domain.shared.Distance;
import com.paceai.domain.training.plan.GoalDistance;
import com.paceai.domain.training.plan.PlanStatus;
import com.paceai.domain.training.plan.TrainingPlan;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TrainingPlanTests {

    @Test
    void shouldCreateNewTrainingPlan() {
        AthleteId athleteId = AthleteId.create();
        Distance weeklyVolume = Distance.ofKilometers(20.0);
        LocalDate startDate = LocalDate.now();
        GoalDistance goal = GoalDistance.FIVE_K;
        LocalDate raceDate = startDate.plusWeeks(8);

        TrainingPlan plan = TrainingPlan.create(
                athleteId,
                weeklyVolume,
                startDate,
                goal,
                raceDate
        );

        assertThat(plan.id()).isNotNull();
        assertThat(plan.athleteId()).isEqualTo(athleteId);
        assertThat(plan.weeklyVolume()).isEqualTo(weeklyVolume);
        assertThat(plan.startDate()).isEqualTo(startDate);
        assertThat(plan.goalDistance()).isEqualTo(goal);
        assertThat(plan.raceDate()).isEqualTo(raceDate);
        assertThat(plan.status()).isEqualTo(PlanStatus.DRAFT);
    }

    @Test
    void shouldAllowSafeIncreaseUnder15Percent() {
        AthleteId athleteId = AthleteId.create();
        Distance previousVolume = Distance.ofKilometers(20.0);
        
        TrainingPlan previousPlan = TrainingPlan.create(
                athleteId,
                previousVolume,
                LocalDate.now().minusWeeks(8),
                GoalDistance.FIVE_K,
                LocalDate.now()
        );

        Distance newVolume = Distance.ofKilometers(22.0); // 10% increase

        assertThatCode(() -> TrainingPlan.createWithValidation(
                athleteId,
                newVolume,
                LocalDate.now(),
                GoalDistance.TEN_K,
                LocalDate.now().plusWeeks(10),
                previousPlan
        )).doesNotThrowAnyException();
    }

    @Test
    void shouldRejectExcessiveIncreaseOver15Percent() {
        AthleteId athleteId = AthleteId.create();
        Distance previousVolume = Distance.ofKilometers(20.0);

        TrainingPlan previousPlan = TrainingPlan.create(
                athleteId,
                previousVolume,
                LocalDate.now().minusWeeks(8),
                GoalDistance.FIVE_K,
                LocalDate.now()
        );

        Distance newVolume = Distance.ofKilometers(24.0); // 20% increase

        assertThatThrownBy(() -> TrainingPlan.createWithValidation(
                athleteId,
                newVolume,
                LocalDate.now(),
                GoalDistance.TEN_K,
                LocalDate.now().plusWeeks(10),
                previousPlan
        ))
        .isInstanceOf(ExcessiveLoadDomainException.class)
        .hasMessageContaining("15%");
    }
}
