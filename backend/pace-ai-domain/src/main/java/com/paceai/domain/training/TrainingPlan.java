package com.paceai.domain.training;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import com.paceai.domain.exceptions.ExcessiveLoadDomainException;
import com.paceai.domain.shared.Distance;
import com.paceai.domain.shared.Identifiers;

public final class TrainingPlan {

    private final UUID id;
    private final UUID athleteId;
    private final Distance weeklyVolume;
    private final LocalDate startDate;

    private TrainingPlan(UUID id, UUID athleteId, Distance weeklyVolume, LocalDate startDate) {
        this.id = Objects.requireNonNull(id, "O ID não pode ser nulo");
        this.athleteId = Objects.requireNonNull(athleteId, "O ID do Atleta deve ser fornecido");
        this.weeklyVolume = Objects.requireNonNull(weeklyVolume, "O volume de treinos da semana não pode estar vazio");
        this.startDate = Objects.requireNonNull(startDate, "A data de inicio não pode ser nula.");
    }

    public static TrainingPlan create(UUID id, UUID athleteId, Distance weeklyVolume, LocalDate startDate) {
        return new TrainingPlan(id, athleteId, weeklyVolume, startDate);
    }

    public static TrainingPlan createForNewAthlete(UUID athleteId, Distance weeklyVolume) {
        return new TrainingPlan(Identifiers.newId(), athleteId, weeklyVolume, LocalDate.now());
    }

    public UUID getId() {
        return id;
    }

    public UUID getAthleteId() {
        return athleteId;
    }

    public Distance getWeeklyVolume() {
        return weeklyVolume;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void validateAgainstPreviousPlan(TrainingPlan previousPlan) {
        double previousVolumeKm = previousPlan.getWeeklyVolume().getValueInKilometers();

        if (previousVolumeKm <= 0) {
            return;
        }

        double currentVolumeKm = this.weeklyVolume.getValueInKilometers();
        double percentageIncrease = ((currentVolumeKm - previousVolumeKm) / previousVolumeKm) * 100;

        if (percentageIncrease > 15) {
            throw new ExcessiveLoadDomainException(
                    String.format("Carga excessiva detectada: %.2f%% aumento excede 15%% do limite saudável " +
                                    "Volume anterior: %.2f km, Novo volume: %.2f km",
                            percentageIncrease, previousVolumeKm, currentVolumeKm)
            );
        }
    }
}
