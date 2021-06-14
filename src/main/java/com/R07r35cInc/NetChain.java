package com.R07r35cInc;

import java.util.Date;

public class NetChain<T> extends SimpleChain<T> {

    public Block mineBlock(Block lastBlock, T data) { //here we can use a prof=true/false to decide use it or not
        Long timestamp = new Date().getTime();
        String previousHash = lastBlock.getHash();
        long index = lastBlock.getIndex()+1;
        return Miner.proofOfWork(timestamp, previousHash, data, index);
    }

}
