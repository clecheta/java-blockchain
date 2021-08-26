package com.R07r35cInc.wallet;

import com.R07r35cInc.util.CryptoHash;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
@JsonPropertyOrder({"date", "amount", "address", "signature"})
public class Input {
    public Date date; //FIXME error on parsing, its producing: 1629981890124 | instead of: 2021-08-26T12:44:50.124+00:00
    public BigDecimal amount;
    public String address;
    public String signature;

    public Input() {}

    public Input(Date date, BigDecimal amount, String address, String signature) {
        this.date = date;
        this.amount = amount;
        this.address = address;
        this.signature = signature;
    }

    //TODO: Make test for equals() and hasCode() comparison implementation

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Input)) return false;
        Input input = (Input) o;
        String thisDate = CryptoHash.dateFormat.format(date);
        String oDate = CryptoHash.dateFormat.format(input.date);
        return Objects.equals(thisDate, oDate) &&
                Objects.equals(amount, input.amount) &&
                Objects.equals(address, input.address) &&
                Objects.equals(signature, input.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, amount, address, signature);
    }
}
