package com.R07r35cInc;

import java.util.Date;

public class Blockchain<T> {

    //This is here instead of Block class because we could not use static method with generic type
    public Block mineBlock(Block lastBlock, T data) {
        Long timestamp = new Date().getTime();
        String previousHash = lastBlock.getHash();
        String hash = CryptoHash.calculateHash(timestamp, previousHash, data);
        return new Block(
                timestamp,
                previousHash,
                hash,
                data
        );
    }

}