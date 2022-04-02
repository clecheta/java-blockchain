package com.R07r35cInc.cryptocurrency;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
@JsonPropertyOrder({"id", "type", "input", "outputMap", "tx"})
public class PayTypeTransaction implements Tx {
    public String tx = "8594486971833484244";

    private final Tx transaction;

    private PayTypeEnum type;

    public PayTypeTransaction() {
        this.transaction = new Transaction();
    }

    protected PayTypeTransaction(Tx transaction, PayTypeEnum type) {
        this.transaction = transaction;
        this.type = type;
    }

    @Override
    public UUID getId() {
        return this.transaction.getId();
    }

    @Override
    public void setId(UUID id) {
        this.transaction.setId(id);
    }

    @Override
    public Input getInput() {
        return this.transaction.getInput();
    }

    @Override
    public void setInput(Input input) {
        this.transaction.setInput(input);
    }

    @Override
    public Map<String, BigDecimal> getOutputMap() {
        return this.transaction.getOutputMap();
    }

    @Override
    public void setOutputMap(Map<String, BigDecimal> outputMap) {
        this.transaction.setOutputMap(outputMap);
    }

    @Override
    public void update(Wallet senderWallet, String recipient, BigDecimal amount) {
        this.transaction.update(senderWallet, recipient, amount);
    }

    public PayTypeEnum getType() {
        return type;
    }

}
