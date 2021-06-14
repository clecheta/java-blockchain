package com.R07r35cInc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Block<T> {
    private Date date;
    private String previousHash;
    private String hash;
    private T data;
    private Long nonce;
    private Long index;

    /** you must save the block with the timestamp that generated the crypto hash */
    Block(Date date, String previousHash, String hash, T data, long index, long nonce) {
        this.date = date;
        this.previousHash = previousHash;
        this.hash = hash;
        this.data = data;
        this.index = index;
        this.nonce = nonce;
    }

    public static Block genesis() {
        String beforeWorldEvenExists = "00/00/0000 00:00:00";
        try {
            return new Block(
                    new SimpleDateFormat(CryptoHash.timestampFormat).parse(beforeWorldEvenExists),
                    "Yes-I-am-coming-soon.Amen.Come-Lord-Jesus",
                    "The-stone-the-builders-rejected-has-become-the-cornerstone",
                    "Book of Genesis", 0, 0000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date getDate() {
        return date;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public T getData() {
        return data;
    }

    public long getIndex() {
        return index;
    }

    public long getNonce() {
        return nonce;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Block)) return false;
        Block<?> block = (Block<?>) o;
        return date.equals(block.date) && Objects.equals(previousHash, block.previousHash) && Objects.equals(hash, block.hash) && Objects.equals(data, block.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, previousHash, hash, data);
    }

    /* these sets methods were create for test purpose only
     * @See BlockChainTest.fakeGenesisBlock()
     * @See BlockChainTest.isValidChain()
     * @See ReplaceChainTest.longerButInvalidChain()
     */
    public void setData(T data) {
        this.data = data;
        throw new RuntimeException("you cannot change a block data");
    }
    public void setPreviousHash(String aHash) {
        this.previousHash = aHash;
        throw new RuntimeException("please dont try to change the previous hash");
    }

}