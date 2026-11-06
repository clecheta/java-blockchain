package com.R07r35cInc.blockchain;

import com.R07r35cInc.util.CryptoHash;
import com.R07r35cInc.util.Parse;

import java.util.Date;

/**
 * Implements a Proof of Work consensus mechanism
 * for transactions created in the network.
 * <p>
 * This is used for Unit testing, in a real blockchain network,
 * miners would listen for transactions nodes created and then perform
 * POW and create a block, which would be sent back to network, and validated
 * by each node.
 *
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
public class Miner<T> {

    public static final String nonceKey = "000";

    static Block proofOfWork(Date date, String previousHash, Object data, long number) {
        long nonce = 0;
        boolean nonceFound = false;
        String nonceHash = "";
        String serializedData = Parse.toJson(data);

        while (!nonceFound) {
            date = new Date();
            nonce++;
            nonceHash = CryptoHash.calculateHashFromSerializedData(
                    date, previousHash, serializedData, number, nonce);
            nonceFound = nonceHash.startsWith(nonceKey);
        }

        //TODO refactor to no use proofOfWork as static method and return Block<T>
        return new Block(date,
                previousHash, nonceHash,
                data, number, nonce
        );
    }

}
