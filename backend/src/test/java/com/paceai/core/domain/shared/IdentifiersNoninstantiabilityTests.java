package com.paceai.core.domain.shared;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IdentifiersNoninstantiabilityTests {

    @Test
    void shouldThrowAssertionErrorWhenTryingToInstantiate() throws Exception {
        Constructor<Identifiers> constructor = Identifiers.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        assertThatThrownBy(() -> constructor.newInstance())
                .isInstanceOf(InvocationTargetException.class)
                .hasCauseExactlyInstanceOf(AssertionError.class)
                .hasRootCauseMessage("Identifiers is a utility class and cannot be instantiated");
    }
}
