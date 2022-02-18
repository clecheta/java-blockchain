package com.R07r35cInc.blockchain;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * A Custom type<T> blockchain to be used with Peer Network API.
 * It does implement proofOfWork.
 *
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
public class NetChain<T> extends BlockChain<T> {

    /** Identify sending node */
    public String origin;

    public Block<T> mineBlock(Block lastBlock, T data) { //here we can use a prof=true/false to decide use it or not
        Date date = new Date();
        String previousHash = lastBlock.getHash();
        long index = lastBlock.getIndex()+1;
        return (Block<T>) Miner.proofOfWork(date, previousHash, data, index);
    }

    /**
     * Used to avoid redundant interactions on replace chain
     */
    public Boolean isValidForReplacement(String currentHost) {
        Boolean isValid = false;
        if (this != null && this.origin != null && getChain() != null) {
            isValid = !this.origin.equals(currentHost);
            //System.out.println(LocalDateTime.now() + " DEBUG [CryptoChain] " + currentHost + " isValid: " + isValid);
        }
        return isValid;
    }

}
