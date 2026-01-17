package com.paceai.domain.training;

import com.paceai.domain.exceptions.ExcessiveLoadDomainException;
import com.paceai.domain.shared.Distance;
import com.paceai.domain.shared.Identifiers;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TrainingPlanTests {

    @Test
    void shouldAllowSafeIncreaseUnder15Percent() {
        Distance previousVolume = Distance.ofKilometers(20.0);
        Distance newVolume = Distance.ofKilometers(22.0);

        TrainingPlan previousPlan = TrainingPlan.create(
                Identifiers.newId(),
                Identifiers.newId(),
                previousVolume,
                LocalDate.now()
        );

        TrainingPlan newPlan = TrainingPlan.create(
                Identifiers.newId(),
                Identifiers.newId(),
                newVolume,
                LocalDate.now().plusWeeks(1)
        );

        assertThatCode(() -> newPlan.validateAgainstPreviousPlan(previousPlan))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldRejectExcessiveIncreaseOver15Percent() {
        Distance previousVolume = Distance.ofKilometers(20.0);
        Distance newVolume = Distance.ofKilometers(24.0);

        TrainingPlan previousPlan = TrainingPlan.create(
                Identifiers.newId(),
                Identifiers.newId(),
                previousVolume,
                LocalDate.now()
        );

        TrainingPlan newPlan = TrainingPlan.create(
                Identifiers.newId(),
                Identifiers.newId(),
                newVolume,
                LocalDate.now().plusWeeks(1)
        );

        assertThatThrownBy(() -> newPlan.validateAgainstPreviousPlan(previousPlan))
                .isInstanceOf(ExcessiveLoadDomainException.class)
                .hasMessageContaining("15%");
    }

    @Test
    void shouldAllowZeroPreviousVolumeForNewAthletes() {
        Distance previousVolume = Distance.ofKilometers(0.0);
        Distance newVolume = Distance.ofKilometers(10.0);

        TrainingPlan previousPlan = TrainingPlan.create(
                Identifiers.newId(),
                Identifiers.newId(),
                previousVolume,
                LocalDate.now()
        );

        TrainingPlan newPlan = TrainingPlan.create(
                Identifiers.newId(),
                Identifiers.newId(),
                newVolume,
                LocalDate.now().plusWeeks(1)
        );

        assertThatCode(() -> newPlan.validateAgainstPreviousPlan(previousPlan))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldAcceptExactly15PercentIncrease() {
        Distance previousVolume = Distance.ofKilometers(20.0);
        Distance newVolume = Distance.ofKilometers(23.0);

        TrainingPlan previousPlan = TrainingPlan.create(
                Identifiers.newId(),
                Identifiers.newId(),
                previousVolume,
                LocalDate.now()
        );

        TrainingPlan newPlan = TrainingPlan.create(
                Identifiers.newId(),
                Identifiers.newId(),
                newVolume,
                LocalDate.now().plusWeeks(1)
        );

        assertThatCode(() -> newPlan.validateAgainstPreviousPlan(previousPlan))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldUseDescriptiveFactoryMethodForNewAthletes() {
        UUID athleteId = Identifiers.newId();
        Distance weeklyVolume = Distance.ofKilometers(10.0);

        TrainingPlan plan = TrainingPlan.createForNewAthlete(athleteId, weeklyVolume);

        org.assertj.core.api.Assertions.assertThat(plan.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(plan.getAthleteId()).isEqualTo(athleteId);
        org.assertj.core.api.Assertions.assertThat(plan.getWeeklyVolume()).isEqualTo(weeklyVolume);
        org.assertj.core.api.Assertions.assertThat(plan.getStartDate()).isNotNull();
    }
}
