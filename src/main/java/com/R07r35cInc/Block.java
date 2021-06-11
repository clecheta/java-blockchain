package com.R07r35cInc;

import java.util.Objects;

public class Block<T> {
    private long timestamp;
    private String previousHash;
    private String hash;
    private T data;

    private String nonce;
    private long index;

    Block(Long timestamp, String previousHash, String hash, T data, long index, String nonce) {
        this.timestamp = timestamp;
        this.previousHash = previousHash;
        this.hash = hash;
        this.data = data;
        this.index = index;
        this.nonce = nonce;
    }

    public static Block genesis() {
        Long beforeWorldEvenExists = 0L;
        return new Block(
                beforeWorldEvenExists,
                "Yes-I-am-coming-soon.Amen.Come-Lord-Jesus",
                "The-stone-the-builders-rejected-has-become-the-cornerstone",
                "Book of Genesis", 0, "0");
    }

    public long getTimestamp() {
        return timestamp;
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

    public String getNonce() {
        return nonce;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Block)) return false;
        Block<?> block = (Block<?>) o;
        return timestamp == block.timestamp && Objects.equals(previousHash, block.previousHash) && Objects.equals(hash, block.hash) && Objects.equals(data, block.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, previousHash, hash, data);
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