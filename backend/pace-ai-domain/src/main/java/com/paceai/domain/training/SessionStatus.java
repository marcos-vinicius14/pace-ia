package com.paceai.domain.training;

/**
 * Session Status Enum with State Machine transitions.
 */
public enum SessionStatus {
    PENDING {
        @Override
        public boolean canTransitionTo(SessionStatus target) {
            return target == COMPLETED || target == MISSED || target == SKIPPED;
        }
    },
    COMPLETED {
        @Override
        public boolean canTransitionTo(SessionStatus target) {
            return false;
        }
    },
    MISSED {
        @Override
        public boolean canTransitionTo(SessionStatus target) {
            return false;
        }
    },
    SKIPPED {
        @Override
        public boolean canTransitionTo(SessionStatus target) {
            return false;
        }
    };

    public abstract boolean canTransitionTo(SessionStatus target);

    public SessionStatus transitionTo(SessionStatus target) {
        if (!canTransitionTo(target)) {
            throw new IllegalStateException(
                    String.format("Invalid transition from %s to %s", this, target)
            );
        }
        return target;
    }
}
