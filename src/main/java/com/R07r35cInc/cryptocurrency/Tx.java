package com.R07r35cInc.cryptocurrency;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tx")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Transaction.class, name = "transaction"),
        @JsonSubTypes.Type(value = PayTypeTransaction.class, name = "payType"),
})
public interface Tx {
    UUID getId();

    void setId(UUID id);

    Input getInput();

    void setInput(Input input);

    Map<String, BigDecimal> getOutputMap();

    void setOutputMap(Map<String, BigDecimal> outputMap);

    void update(Wallet senderWallet, String recipient, BigDecimal amount);
}
