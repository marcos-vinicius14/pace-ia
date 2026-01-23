package com.paceai.domain.shared;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Email Value Object.
 * <p>
 * Immutable value object representing a validated email address.
 * </p>
 */
public final class Email {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );

    private final String value;

    private Email(String value) {
        this.value = value.toLowerCase();
    }

    public static Result<Email> create(String value) {
        if (value == null || value.isBlank()) {
            return Result.failure("Email cannot be empty");
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            return Result.failure("Invalid email format: " + value);
        }
        return Result.success(new Email(value));
    }

    /**
     * @deprecated Use create(String) instead. This method throws exceptions.
     */
    @Deprecated
    public static Email of(String value) {
        return create(value).orElseThrow(IllegalArgumentException::new);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
