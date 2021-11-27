package com.R07r35cInc.cryptocurrency;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
public interface Tx {
    UUID getId();
    void setId(UUID id);

    Input getInput();
    void setInput(Input input);

    Map<String, BigDecimal> getOutputMap();
    void setOutputMap(Map<String, BigDecimal> outputMap);

    PayTypeEnum getType();
}
