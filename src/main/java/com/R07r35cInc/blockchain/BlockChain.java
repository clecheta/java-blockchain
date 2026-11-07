package com.R07r35cInc.blockchain;

import com.R07r35cInc.util.CryptoHash;
import com.R07r35cInc.util.Parse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A generic type<T> blockchain to be directly used inside any application.
 * Do not make usage of proofOfWork algorithm.
 *
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
public class BlockChain<T> implements Chain<T> {

    private List<Block<T>> chain;

    public BlockChain() {
        this.chain = new ArrayList<Block<T>>();
        this.chain.add(Block.genesis());
    }

    /**
     * add a new block to the chain based on the last block that came before it
     */
    public Block addBlock(T data) {
        Block lastBlock = this.chain.get(this.chain.size() - 1); //the end of the local chain
        Block newBlock = mineBlock(lastBlock, data);
        this.chain.add(newBlock);
        return newBlock;
    }

    //This is here instead of Block class because we could not use static method with generic type
    public Block mineBlock(Block lastBlock, T data) {
        Date timestamp = new Date();
        String previousHash = lastBlock.getHash();
        long number = lastBlock.getNumber() + 1;
        long nonce = 00;
        String hash = CryptoHash.calculateHash(timestamp, previousHash, data, number, nonce);
        return new Block(timestamp, previousHash, hash, data, number, nonce);
    }

    public Boolean isValidChain() {
        return isValidChain(this.chain);
    }

    /**
     * Chain Validation - inspect a Blockchain (inspect each block was construct correctly)
     */
    public Boolean isValidChain(List<Block<T>> chain) {
        if (!Block.genesis().equals(chain.get(0))) {
            return false; //genesis block validation
        }
        Block block;
        String lastHash;
        String validatedHash;
        //loop through blockchain to check hashes
        for (int i = 1; i < chain.size(); i++) { //skip the genesis block at position 0 (already validated in 1st line)
            block = chain.get(i);
            lastHash = chain.get(i - 1).getHash();

            /** validate actual lashHash reference (links to block-to-block) */
            if (!lastHash.equals(block.getPreviousHash())) {
                System.out.println(LocalDateTime.now() + " ERROR [Blockchain] Previous-hashes-not-equal");
                return false;
            }

            /** have valid hash */
            validatedHash = CryptoHash.calculateHash(block);
            if (!block.getHash().equals(validatedHash)) {
                System.out.println(LocalDateTime.now() + " ERROR [Blockchain] Current-hashes-not-equal");
                return false;
            }
        }
        return true;
    }

    /**
     * Necessary because of the blockchain network interaction
     * not static method as it is specific to each instance of the blockchain
     **/
    public Boolean replaceChain(List<Block<T>> chain) {
        if (chain == null || (chain.size() <= this.chain.size())) {
            throw new RuntimeException("The incoming chain must be longer");
        }

        if (!this.isValidChain(chain)) {
            throw new RuntimeException("The incoming chain must be valid");
        }

        System.out.println("Replacing chain with: \n" + Parse.toJson(chain));
        this.chain = chain;
        return true;
    }

    /**
     * Hydrate the in-memory chain from a persisted ledger without re-running
     * peer-style validation. PostgreSQL (or an equivalent SoT) already accepted
     * these blocks; boot must not CPU-spin revalidating full history.
     */
    public void hydrateFromLedger(List<Block<T>> chain) {
        if (chain == null || chain.isEmpty()) {
            throw new RuntimeException("Persisted chain must not be empty");
        }
        this.chain = new ArrayList<>(chain);
    }

    /**
     * Append a single peer-mined block when it extends the local tip.
     * Used by incremental Kafka sync instead of full-chain replace.
     */
    public Boolean appendValidatedBlock(Block<T> block) {
        if (block == null) {
            throw new RuntimeException("The incoming block must not be null");
        }
        Block<T> tip = this.chain.get(this.chain.size() - 1);
        if (!tip.getHash().equals(block.getPreviousHash())) {
            throw new RuntimeException("The incoming block previousHash must match the local tip");
        }
        if (block.getNumber() != tip.getNumber() + 1) {
            throw new RuntimeException("The incoming block number must be tip+1");
        }
        String validatedHash = CryptoHash.calculateHash(block);
        if (!block.getHash().equals(validatedHash)) {
            throw new RuntimeException("The incoming block hash is invalid");
        }
        this.chain.add(block);
        return true;
    }

    public List<Block<T>> getChain() {
        return this.chain;
    }

}