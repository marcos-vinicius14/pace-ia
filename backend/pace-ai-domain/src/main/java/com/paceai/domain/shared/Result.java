package com.paceai.domain.shared;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Result Pattern Implementation.
 * <p>
 * Represents the result of an operation that can either succeed (with a value)
 * or fail (with an error message), avoiding exceptions for flow control.
 * </p>
 * @param <T> The type of the value in case of success.
 */
public final class Result<T> {
    private final T value;
    private final String errorMessage;
    private final boolean isSuccess;

    private Result(T value, String errorMessage, boolean isSuccess) {
        this.value = value;
        this.errorMessage = errorMessage;
        this.isSuccess = isSuccess;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, null, true);
    }

    public static <T> Result<T> failure(String errorMessage) {
        return new Result<>(null, errorMessage, false);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isFailure() {
        return !isSuccess;
    }

    public T getValue() {
        if (isFailure()) {
            throw new IllegalStateException("Cannot get value from failure result: " + errorMessage);
        }
        return value;
    }

    public String getErrorMessage() {
        if (isSuccess()) {
            throw new IllegalStateException("Cannot get error message from success result");
        }
        return errorMessage;
    }

    public <U> Result<U> map(Function<T, U> mapper) {
        if (isSuccess()) {
            return Result.success(mapper.apply(value));
        } else {
            return Result.failure(errorMessage);
        }
    }

    public <U> Result<U> flatMap(Function<T, Result<U>> mapper) {
        if (isSuccess()) {
            return mapper.apply(value);
        } else {
            return Result.failure(errorMessage);
        }
    }

    public T orElse(T other) {
        return isSuccess() ? value : other;
    }

    public T orElseGet(Supplier<T> other) {
        return isSuccess() ? value : other.get();
    }
    
    public <X extends Throwable> T orElseThrow(Function<String, X> exceptionSupplier) throws X {
        if (isSuccess()) {
            return value;
        } else {
            throw exceptionSupplier.apply(errorMessage);
        }
    }

    public void ifSuccess(Consumer<T> consumer) {
        if (isSuccess()) {
            consumer.accept(value);
        }
    }

    public void ifFailure(Consumer<String> consumer) {
        if (isFailure()) {
            consumer.accept(errorMessage);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result<?> result = (Result<?>) o;
        return isSuccess == result.isSuccess &&
                Objects.equals(value, result.value) &&
                Objects.equals(errorMessage, result.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, errorMessage, isSuccess);
    }
}
