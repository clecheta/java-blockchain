package com.R07r35cInc;

import java.util.Date;

/**
 * Miner that performs a Proof of Work consensus mechanism
 * transactions created in the network.
 * <p>
 * This is used for Unit testing, in a real blockchain network,
 * miners would listen for transactions nodes created and then perform
 * POW and create a block, which would be sent back to network, and validated
 * by each node.
 */
public class Miner<T> {

    public static final String nonceKey = "00";

    static Block proofOfWork(Date timestamp, String previousHash, Object data, long index) {
        long nonce = 0;
        boolean nonceFound = false;
        String nonceHash = "";

        while (!nonceFound) {
            nonce++;
            nonceHash = CryptoHash.calculateHash(timestamp, previousHash, data, index, nonce);
            nonceFound = nonceHash.startsWith(nonceKey);
        }

        return new Block(timestamp,
                previousHash, nonceHash,
                data, index, nonce
        );
    }

}
