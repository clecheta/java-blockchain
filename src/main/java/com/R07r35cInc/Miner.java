package com.R07r35cInc;

import com.google.gson.Gson;

import java.util.Date;

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

    private String proofOfWork(Block<T> block) {

        String nonceKey = block.getNonce();
        long nonce = 0;
        boolean nonceFound = false;
        String nonceHash = "";

        Gson parser = new Gson();
        String serializedData = parser.toJson(block.getData());
        String base = block.getTimestamp() + block.getPreviousHash() + serializedData;

        while (!nonceFound) {
            nonceHash = CryptoHash.generateHash(base + nonce);
            nonceFound = nonceHash.startsWith(nonceKey);
            nonce++;
        }

        return nonceHash;
    }

    //From Blockchain class
    public Block mineBlock(Block lastBlock, T data) {
        Long timestamp = new Date().getTime();
        String previousHash = lastBlock.getHash();
        long nonce = 0;
        String hash = "";

        boolean nonceFound = false;
        while (!nonceFound) { //proofOfWork
            hash = CryptoHash.calculateHash(timestamp, previousHash, data, Long.toString(nonce));
            nonceFound = hash.startsWith(Block.genesis().getNonce());
            nonce++;
        }

        return new Block(timestamp,
                previousHash, hash,
                data, Long.toString(nonce)
        );
    }

}
