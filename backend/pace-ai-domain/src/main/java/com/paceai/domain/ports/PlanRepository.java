package com.paceai.domain.ports;

import com.paceai.domain.training.Plan;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Plan Repository Port (Interface).
 * <p>
 * This is a Port (in Hexagonal Architecture terms) that defines the contract
 * for persisting and retrieving Training Plans. The implementation lives in
 * the Infrastructure module.
 * </p>
 */
public interface PlanRepository {

    /**
     * Saves a plan to the repository.
     *
     * @param plan the plan to save
     * @return the saved plan
     */
    Plan save(Plan plan);

    /**
     * Finds a plan by its ID.
     *
     * @param id the plan ID
     * @return the plan if found
     */
    Optional<Plan> findById(UUID id);

    /**
     * Finds all plans for a given athlete.
     *
     * @param athleteId the athlete ID
     * @return list of plans
     */
    List<Plan> findByAthleteId(UUID athleteId);

    /**
     * Deletes a plan by its ID.
     *
     * @param id the plan ID
     */
    void deleteById(UUID id);

    /**
     * Checks if a plan exists by its ID.
     *
     * @param id the plan ID
     * @return true if exists
     */
    boolean existsById(UUID id);
}
