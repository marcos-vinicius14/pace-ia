package com.paceai.domain.exceptions;

/**
 * Exception thrown when a business rule is violated.
 */
public class BusinessRuleViolationException extends DomainException {

    private final String rule;

    public BusinessRuleViolationException(String rule, String message) {
        super(message);
        this.rule = rule;
    }

    public String getRule() {
        return rule;
    }
}
