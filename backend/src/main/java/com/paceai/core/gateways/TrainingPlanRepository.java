package com.paceai.core.gateways;

import java.util.List;
import java.util.Optional;

import com.paceai.core.domain.athlete.AthleteId;
import com.paceai.core.domain.shared.Result;
import com.paceai.core.domain.training.plan.TrainingPlan;
import com.paceai.core.domain.training.TrainingPlanId;

/**
 * Training Plan Repository Port (Interface).
 * <p>
 * Defines the contract for persisting and retrieving Training Plans.
 * </p>
 */
public interface TrainingPlanRepository {

    /**
     * Saves a plan to the repository.
     *
     * @param plan the plan to save
     * @return the saved plan
     */
    Result<TrainingPlan> save(TrainingPlan plan);

    /**
     * Finds a plan by its ID.
     *
     * @param id the plan ID
     * @return the plan if found
     */
    Optional<TrainingPlan> findById(TrainingPlanId id);

    /**
     * Finds all plans for a given athlete.
     *
     * @param athleteId the athlete ID
     * @return list of plans
     */
    List<TrainingPlan> findByAthleteId(AthleteId athleteId);

    /**
     * Deletes a plan by its ID.
     *
     * @param id the plan ID
     */
    void deleteById(TrainingPlanId id);

    /**
     * Checks if a plan exists by its ID.
     *
     * @param id the plan ID
     * @return true if exists
     */
    boolean existsById(TrainingPlanId id);
}
