package com.paceai.domain.exceptions;

public final class ExcessiveLoadDomainException extends DomainException {

    public ExcessiveLoadDomainException(String message) {
        super(message);
    }

    public ExcessiveLoadDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
