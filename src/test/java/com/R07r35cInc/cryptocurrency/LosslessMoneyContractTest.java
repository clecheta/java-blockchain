package com.R07r35cInc.cryptocurrency;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LosslessMoneyContractTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void acceptsArbitraryPrecisionDecimalString() throws Exception {
        TransactionCommand command = mapper.readValue(
                "{\"amount\":\"9223372036854775808.1234\"}",
                TransactionCommand.class);

        assertEquals(new BigDecimal("9223372036854775808.1234"), command.amount);
    }

    @Test
    void acceptsBackwardsCompatibleSafeInteger() throws Exception {
        TransactionCommand command = mapper.readValue(
                "{\"amount\":100}",
                TransactionCommand.class);

        assertEquals(new BigDecimal("100"), command.amount);
    }

    @Test
    void rejectsUnsafeJsonNumber() {
        assertThrows(Exception.class, () -> mapper.readValue(
                "{\"amount\":9007199254740992}",
                TransactionCommand.class));
    }

    @Test
    void rejectsFloatingPointJsonNumber() {
        assertThrows(Exception.class, () -> mapper.readValue(
                "{\"amount\":0.1}",
                TransactionCommand.class));
    }
}
