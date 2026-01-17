package com.paceai.domain.shared;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class IdentifiersTests {

    @Test
    void shouldGenerateUniqueIds() {
        Set<String> ids = new HashSet<>();

        for (int i = 0; i < 1000; i++) {
            ids.add(Identifiers.newId().toString());
        }

        assertThat(ids).hasSize(1000);
    }

    @Test
    void shouldGenerateVersion7Ids() {
        var id = Identifiers.newId();
        byte[] bytes = new byte[16];

        long mostSigBits = id.getMostSignificantBits();
        long leastSigBits = id.getLeastSignificantBits();

        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) (mostSigBits >>> (8 * (7 - i)));
            bytes[i + 8] = (byte) (leastSigBits >>> (8 * (7 - i)));
        }

        int version = (bytes[6] & 0xFF) >> 4;
        assertThat(version).isEqualTo(7);
    }

    @Test
    void shouldGenerateTimeOrderedIds() throws InterruptedException {
        var id1 = Identifiers.newId();
        Thread.sleep(1);
        var id2 = Identifiers.newId();

        assertThat(id2).isGreaterThan(id1);
    }
}
