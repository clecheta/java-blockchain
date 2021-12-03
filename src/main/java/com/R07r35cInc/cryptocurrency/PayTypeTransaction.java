package com.R07r35cInc.cryptocurrency;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
public class PayTypeTransaction implements Tx {

    private final Tx transaction;

    private PayTypeEnum payType;

    /*
    protected PayTypeTransaction(Tx transaction) {
        this.transaction = transaction;
    }*/

    public PayTypeTransaction(Tx transaction, PayTypeEnum payType) {
        this.transaction = transaction;
        this.payType = payType;
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

    public PayTypeEnum getPayType() {
        return payType;
    }

}
