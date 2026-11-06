package com.R07r35cInc.cryptocurrency;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Money values are strings at the public API boundary so JavaScript clients
 * cannot silently round values above Number.MAX_SAFE_INTEGER.
 *
 * Small integer JSON numbers remain accepted for backwards compatibility.
 */
public class LosslessBigDecimalDeserializer extends JsonDeserializer<BigDecimal> {

    private static final BigInteger MAX_SAFE_INTEGER = BigInteger.valueOf(9_007_199_254_740_991L);

    @Override
    public BigDecimal deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonToken token = parser.currentToken();
        if (token == JsonToken.VALUE_STRING) {
            String value = parser.getText().trim();
            if (value.isEmpty()) {
                return (BigDecimal) context.handleWeirdStringValue(
                        BigDecimal.class, value, "Money amount must not be empty");
            }
            try {
                return new BigDecimal(value);
            } catch (NumberFormatException exception) {
                return (BigDecimal) context.handleWeirdStringValue(
                        BigDecimal.class, value, "Money amount must be a decimal string");
            }
        }

        if (token == JsonToken.VALUE_NUMBER_INT) {
            BigInteger value = parser.getBigIntegerValue();
            if (value.abs().compareTo(MAX_SAFE_INTEGER) <= 0) {
                return new BigDecimal(value);
            }
            return (BigDecimal) context.handleUnexpectedToken(
                    BigDecimal.class, parser);
        }

        return (BigDecimal) context.handleUnexpectedToken(BigDecimal.class, parser);
    }
}
