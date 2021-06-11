package com.R07r35cInc;

/**
 * Simulates a Miner that performs a Proof of Work consensus mechanism
 * transactions created in the network.
 * <p>
 * This is used for Unit testing, in a real blockchain network,
 * miners would listen for transactions nodes created and then perform
 * POW and create a block, which would be sent back to network, and validated
 * by each node.
 */
public class Miner<T> {

    static Block proofOfWork(Long timestamp, String previousHash, Object data, long index) {
        String nonceKey = Block.genesis().getNonce();
        long nonce = 0;
        boolean nonceFound = false;
        String nonceHash = "";

        while (!nonceFound) {
            nonce++;
            nonceHash = CryptoHash.calculateHash(timestamp, previousHash, data, index, Long.toString(nonce));
            nonceFound = nonceHash.startsWith(nonceKey);
        }

        return new Block(timestamp,
                previousHash, nonceHash,
                data, index, Long.toString(nonce)
        );
    }

}
