package com.paceai.domain.training.plan;

/**
 * Plan Status Enum with State Machine transitions.
 * <p>
 * Implements a state machine pattern for valid status transitions.
 * </p>
 */
public enum PlanStatus {
    DRAFT {
        @Override
        public boolean canTransitionTo(PlanStatus target) {
            return target == ACTIVE;
        }
    },
    ACTIVE {
        @Override
        public boolean canTransitionTo(PlanStatus target) {
            return target == COMPLETED || target == CANCELLED;
        }
    },
    COMPLETED {
        @Override
        public boolean canTransitionTo(PlanStatus target) {
            return false; // Terminal state
        }
    },
    CANCELLED {
        @Override
        public boolean canTransitionTo(PlanStatus target) {
            return false; // Terminal state
        }
    };

    /**
     * Checks if a transition to the target status is valid.
     *
     * @param target the target status
     * @return true if the transition is valid
     */
    public abstract boolean canTransitionTo(PlanStatus target);

    /**
     * Transitions to the target status if valid.
     *
     * @param target the target status
     * @return the target status
     * @throws IllegalStateException if the transition is invalid
     */
    public PlanStatus transitionTo(PlanStatus target) {
        if (!canTransitionTo(target)) {
            throw new IllegalStateException(
                    String.format("Invalid transition from %s to %s", this, target)
            );
        }
        return target;
    }
}