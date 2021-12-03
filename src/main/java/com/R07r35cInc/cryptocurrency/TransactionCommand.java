package com.R07r35cInc.cryptocurrency;

import java.math.BigDecimal;

/**
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
public class TransactionCommand {
    public String recipient;
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
