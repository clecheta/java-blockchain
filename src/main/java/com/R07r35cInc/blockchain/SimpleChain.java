package com.R07r35cInc.blockchain;

import com.R07r35cInc.util.CryptoHash;
import com.R07r35cInc.util.Parse;
import com.R07r35cInc.wallet.Transaction;
import com.R07r35cInc.wallet.Wallet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class SimpleChain<T> implements Blockchain<T> {

    private List<Block<T>> chain;

    public SimpleChain() {
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
        long index = lastBlock.getIndex() + 1;
        long nonce = 00;
        String hash = CryptoHash.calculateHash(timestamp, previousHash, data, index, nonce);
        return new Block(timestamp, previousHash, hash, data, index, nonce);
    }

    //TODO: make a test to ensure this is a non static method
    //FIXME Is it really necessary to pass the chain here as argument?
    public Boolean isValidTransactionData(List<Block<T>> chain) {
        List<Transaction> transactions;
        for (int i = 1; i < chain.size(); i++) { //skip the Genesis block
            Block block = chain.get(i);
            transactions = (List<Transaction>) block.getData();
            Set<Transaction> transactionSet = new HashSet<>();
            int rewardTransactionCount = 0;

            if (transactions != null) { //there is transactions in the block
                for (Transaction transaction : transactions) {
                    if (transaction.input.address.equals(Transaction.REWARD_INPUT)) {
                        rewardTransactionCount += 1;

                        if (rewardTransactionCount > 1) {
                            System.out.println(LocalDateTime.now() + " ERROR [Blockchain] Miner reward exceed limit");
                            return false;
                        }

                        Optional<String> firstKey = transaction.outputMap.keySet().stream().findFirst();
                        BigDecimal firstValue = null;
                        if (firstKey.isPresent()) {
                            firstValue = transaction.outputMap.get(firstKey.get());
                        }
                        if (firstValue == null || firstValue.compareTo(Transaction.MINER_REWARD) != 0) {
                            System.out.println(LocalDateTime.now() + " ERROR [Blockchain] Miner reward amount is invalid");
                            return false;
                        }
                    } else { //not a reward transaction
                        if (!Transaction.isValidTransaction(transaction)) {
                            System.out.println(LocalDateTime.now() + " ERROR [Blockchain] Invalid transaction");
                            return false;
                        }

                        //Validate Input Balances
                        BigDecimal trueBalance = Wallet.calculateBalance(this.getNotTypedChain(),
                                transaction.input.address); //must use this. reference to the chain
                        if (transaction.input.amount.compareTo(trueBalance)!=0) {
                            //Make sure that every transaction input is based on the actual blockchain history
                            System.out.println(LocalDateTime.now() + " ERROR [Blockchain] Invalid input amount");
                            //return false; FIXME
                        }

                        //Prevent Duplicate Transactions in Block
                        if (transactionSet.contains(transaction)) {
                            System.out.println(LocalDateTime.now() +
                                    " ERROR [Blockchain] An identical transaction appears more than once in the block");
                            return false;
                        } else {
                            transactionSet.add(transaction);
                        }
                    }
                }
            }
        }
        return true;
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
    public Boolean replaceChain(List<Block<T>> chain, Boolean validateTransactions) {
        if (chain == null || (chain.size() <= this.chain.size())) {
            throw new RuntimeException("The incoming chain must be longer");
        }

        if (!this.isValidChain(chain)) {
            throw new RuntimeException("The incoming chain must be valid");
        }

        //This can be specialized in a CryptoChain object only maybe...
        if (validateTransactions && !this.isValidTransactionData(chain)) {
            throw new RuntimeException("The incoming chain has invalid data");
        }

        System.out.println("Replacing chain with: \n" + Parse.toJson(chain));
        this.chain = chain;
        return true;
    }

    public List<Block<T>> getChain() {
        return this.chain;
    }

    private List<Block> getNotTypedChain() {
        List<Block> blockList = new ArrayList<>();
        blockList.addAll(this.chain);
        return blockList;
    }

}