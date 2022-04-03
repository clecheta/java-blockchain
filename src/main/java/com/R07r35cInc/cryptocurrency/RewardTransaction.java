package com.R07r35cInc.cryptocurrency;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
@JsonPropertyOrder({"id", "type", "input", "outputMap", "serial"})
public class RewardTransaction implements Tx {
    public String serial = "5179406239971980314";

    private final Tx transaction;

    public String type = "REWARD";

    public RewardTransaction() {
        this.transaction = new Transaction();
    }

    /**
     * Override constructor to work with mining transactions
     */
    public RewardTransaction(Map<String, BigDecimal> outputMap, Input input) {
        this();
        this.setOutputMap(outputMap);
        this.setInput(input);
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

}
