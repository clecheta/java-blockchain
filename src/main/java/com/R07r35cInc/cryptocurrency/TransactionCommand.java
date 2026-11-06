package com.R07r35cInc.cryptocurrency;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;

/**
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
public class TransactionCommand {
    /** From: origin */
    public String uid;
    /** To: destination */
    public String recipient;
    @JsonDeserialize(using = LosslessBigDecimalDeserializer.class)
    public BigDecimal amount;
    public PayTypeEnum type;

    public TransactionCommand() {}

    public TransactionCommand(String recipient, BigDecimal amount) {
        this.recipient = recipient;
        this.amount = amount;
    }

    public TransactionCommand(String recipient, BigDecimal amount, PayTypeEnum type) {
        this.recipient = recipient;
        this.amount = amount;
        this.type = type;
    }
}
