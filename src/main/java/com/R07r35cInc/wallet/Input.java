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
    /* Error on parsing date field in api,
         its producing: 1629981890124 | instead of: 2021-08-26T12:44:50.124+00:00

      Possible Solutions:
      1- annotate with @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CryptoHash.stringDateFormat)
      2- objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      3- Separated Kafka channel with typed cryptochain messaged (worked as expected)
      4- Use only string serializer/deserializer in both channels and handle the income message with the util.Parse
     */
    public Date date;
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
