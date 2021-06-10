package com.R07r35cInc;

import java.util.Date;
import java.util.Objects;

public class Block<T> {
    private long timestamp;
    private String previousHash;
    private String hash;
    private T data;

    Block(Long timestamp, String previousHash, String hash, T data) {
        this.timestamp = timestamp;
        this.previousHash = previousHash;
        this.hash = hash;
        this.data = data;
    }

    public static Block genesis() {
        return new Block(
                new Date().getTime(),
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
}