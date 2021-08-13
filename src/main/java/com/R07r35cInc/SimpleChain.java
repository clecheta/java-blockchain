package com.R07r35cInc;

import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.*;

public class SimpleChain<T> implements Blockchain<T> {

    private List<Block<T>> chain;

    public SimpleChain() {
        this.chain = new ArrayList<Block<T>>();
        this.chain.add(Block.genesis());
    }

    /** add a new block to the chain based on the last block that came before it */
    public Block addBlock(T data) {
        Block lastBlock = this.chain.get(this.chain.size()-1); //the end of the local chain
        Block newBlock = mineBlock(lastBlock, data);
        this.chain.add(newBlock);
        return newBlock;
    }

    //This is here instead of Block class because we could not use static method with generic type
    public Block mineBlock(Block lastBlock, T data) {
        Date timestamp = new Date();
        String previousHash = lastBlock.getHash();
        long index = lastBlock.getIndex()+1;
        long nonce = 00;
        String hash = CryptoHash.calculateHash(timestamp, previousHash, data, index, nonce);
        return new Block(timestamp, previousHash, hash, data, index, nonce);
    }

    public Boolean isValidChain() {
        return isValidChain(this.chain);
    }

    /** Chain Validation - inspect a Blockchain (inspect each block was construct correctly) */
    public Boolean isValidChain(List<Block<T>> chain) {
        if (!Block.genesis().equals(chain.get(0))) {
            return false; //genesis block validation
        }
        Block block;
        String lastHash;
        String validatedHash;
        //loop through blockchain to check hashes
        for(int i=1; i < chain.size(); i++) { //skip the genesis block at position 0 (already validated in 1st line)
            block = chain.get(i);
            lastHash = chain.get(i-1).getHash();

            /** validate actual lashHash reference (links to block-to-block) */
            if(!lastHash.equals(block.getPreviousHash()) ) {
                System.out.println(LocalDateTime.now() + "|Previous-hashes-not-equal");
                return false;
            }

            /** have valid hash */
            validatedHash = CryptoHash.calculateHash(block);
            if(!block.getHash().equals(validatedHash) ){
                System.out.println(LocalDateTime.now() + "|Current-hashes-not-equal");
                return false;
            }
        }
        return true;
    }

    /** Necessary because of the blockchain network interaction
     * not static method as it is specific to each instance of the blockchain **/
    public void replaceChain(List<Block<T>> chain) {
        if (chain == null || (chain.size() <= this.chain.size())) {
            throw new RuntimeException("The incoming chain must be longer");
        }

        if (!this.isValidChain(chain)) {
            throw new RuntimeException("The incoming chain must be valid");
        }

        String chainJson = new GsonBuilder().setPrettyPrinting().create().toJson(chain);
        System.out.println("Replacing chain with: \n" + chainJson );
        this.chain = chain;
    }

    public List<Block<T>> getChain() {
        return this.chain;
    }

}