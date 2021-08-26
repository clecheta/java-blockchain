package com.R07r35cInc.blockchain;

import java.util.Date;

public class NetChain<T> extends SimpleChain<T> {

    public Block mineBlock(Block lastBlock, T data) { //here we can use a prof=true/false to decide use it or not
        Date date = new Date();
        String previousHash = lastBlock.getHash();
        long index = lastBlock.getIndex()+1;
        return Miner.proofOfWork(date, previousHash, data, index);
    }

}
