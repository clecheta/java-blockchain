package com.R07r35cInc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Blockchain<T> {

    private List<Block> chain;

    Blockchain() {
        this.chain = new ArrayList<Block>();
        this.chain.add(Block.genesis());
    }

    /** add a new block to the chain based on the last block that came before it */
    public void addBlock(T data) {
        Block lastBlock = this.chain.get(this.chain.size()-1); //the end of the local chain
        Block newBlock = mineBlock(lastBlock, data);
        this.chain.add(newBlock);
    }

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

    /** Chain Validation - inspect a Blockchain (inspect each block was construct correctly) */
    public Boolean isValidChain() {
        if (!Block.genesis().equals(chain.get(0))) return false; //genesis block validation
        Block block;
        String lastHash;
        String validatedHash;
        //loop through blockchain to check hashes
        for(int i=1; i < chain.size(); i++) { //0 index already validated in the first line of the method
            block = chain.get(i);
            lastHash = chain.get(i-1).getHash();

            //compare registered hash and calculated hash:
            validatedHash = CryptoHash.calculateHash(block);
            if(!block.getHash().equals(validatedHash) ){
                System.out.println(LocalDateTime.now() + "|Current-hashes-not-equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!lastHash.equals(block.getPreviousHash()) ) {
                System.out.println(LocalDateTime.now() + "|Previous-hashes-not-equal");
                return false;
            }
        }
        return true;
    }

    public List<Block> getChain() {
        return chain;
    }

}