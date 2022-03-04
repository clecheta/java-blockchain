package com.R07r35cInc.blockchain;

import com.R07r35cInc.util.CryptoHash;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

/**
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
@JsonPropertyOrder({"date", "previousHash", "hash", "data", "nonce", "number"})
public class Block<T> {
    private Date date;
    private String previousHash;
    private String hash;
    private T data;
    private Long nonce;

    //Database usage
    private Long number;
    /* TODO change this to a number (chainId)
     * Thinking in multiple chains in the same network I can add a Chain Object to the models
     * with name, type and id also a start and end date
     */
    private String chain;

    /**
     * you must save the block with the timestamp that generated the crypto hash
     */
    public Block(Date date, String previousHash, String hash, T data, Long number, Long nonce) {
        this.date = date;
        this.previousHash = previousHash;
        this.hash = hash;
        this.data = data;
        this.number = number;
        this.nonce = nonce;
    }

    public Block() {
    }

    public static Block genesis() {
        String beforeWorldEvenExists = "18/06/2008 00:00:00";
        try {
            return new Block(
                    CryptoHash.dateFormat.parse(beforeWorldEvenExists),
                    "Yes-I-am-coming-soon.Amen.Come-Lord-Jesus",
                    "The-stone-the-builders-rejected-has-become-the-cornerstone",
                    null, 0L, 0L);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String aHash) {
        this.previousHash = aHash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public T getData() {
        return data;
    }

    /* Setter methods and default constructor added to be possible to
     * unmarshaller block objects thru kafka channels */

    public void setData(T data) {
        this.data = data;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getNonce() {
        return nonce;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Block)) return false;
        Block<?> block = (Block<?>) o;
        String thisDate = CryptoHash.dateFormat.format(date);
        String oDate = CryptoHash.dateFormat.format(block.getDate());
        return Objects.equals(thisDate, oDate) &&
                Objects.equals(previousHash, block.previousHash) &&
                Objects.equals(hash, block.hash) &&
                Objects.equals(data, block.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, previousHash, hash, data);
    }

}
