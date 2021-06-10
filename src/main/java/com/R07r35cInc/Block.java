package com.R07r35cInc;

import java.util.Objects;

public class Block<T> {
    private long timestamp;
    private String previousHash;
    private String hash;
    private T data;

    public Block(Long timestamp, String previousHash, String hash, T data) {
        this.timestamp = timestamp;
        this.previousHash = previousHash;
        this.hash = hash;
        this.data = data;
    }

    public static Block genesis() {
        Long beforeWorldEvenExists = 0L;
        return new Block(
                beforeWorldEvenExists,
                "Yes-I-am-coming-soon.Amen.Come-Lord-Jesus",
                "The-stone-the-builders-rejected-has-become-the-cornerstone",
                "Book of Genesis 1.1");
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

    /* the set methods here were create for test purpose only */

    public void setData(T data) { //@see BlockChainTest.fakeGenesisBlock()
        this.data = data;
    }
    public void setPreviousHash(String aHash) { //@see BlockChainTest.isValidChain()
        this.previousHash = aHash;
    }

}