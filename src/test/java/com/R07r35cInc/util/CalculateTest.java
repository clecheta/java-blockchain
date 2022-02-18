package com.R07r35cInc.util;

import com.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
public class CalculateTest extends BaseTest {

    Map<String, BigDecimal> amountValuesMap;

    @BeforeEach
    void init() {
        amountValuesMap = new HashMap<String, BigDecimal>();
        for (int i = 0; i < 100; i++) {
            amountValuesMap.put("index" + i, new BigDecimal(i));
        }
    }

    @Test
    void sumAmountValuesTotal() {
        BigDecimal actualSum = Calculate.mapTotal(amountValuesMap);
        BigDecimal expectedSum = new BigDecimal(4950);
        assertTrue(amountValuesMap.size() == 100);
        assertEquals(expectedSum, actualSum, "must sum all elements in the map");
    }

    //TODO create a test with a real transactionMap use case

}
