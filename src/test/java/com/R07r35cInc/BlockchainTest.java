package com.R07r35cInc;


import com.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class BlockchainTest extends BaseTest {

    Blockchain blockchain;

    @BeforeEach
    void init() {
        blockchain = new Blockchain();
    }

    @Test
    void testChain() {
        assertTrue(blockchain.getChain() instanceof ArrayList, "contains a `chain` List instance");
        Block actualBlock = (Block) blockchain.getChain().get(0);
        Block expectedBlock = Block.genesis();
        assertEquals(expectedBlock, actualBlock, "starts with the genesis block");
        BigDecimal newData = BigDecimal.TEN;
        blockchain.addBlock(newData);
        Block lastBlock = (Block) blockchain.getChain().get(blockchain.getChain().size()-1);
        assertEquals(newData, lastBlock.getData(), "adds a new block to the chain");
    }

    @Test
    void fakeGenesisBlock() {
        ((Block)blockchain.getChain().get(0)).setData("fake-genesis");
        assertFalse(blockchain.isValidChain(), "the chain does not start with the genesis block");
    }

    @Test
    void isValidChain() {
        blockchain.addBlock("Exodus");
        blockchain.addBlock("Leviticus");
        blockchain.addBlock("A LEGO GIFT FROM GOD!");

        String originalHash = ((Block)blockchain.getChain().get(2)).getPreviousHash();
        ((Block)blockchain.getChain().get(2)).setPreviousHash("broken-previous-hash");
        assertFalse(blockchain.isValidChain(), "previous hash reference has changed");
        ((Block)blockchain.getChain().get(2)).setPreviousHash(originalHash);
        assertTrue(blockchain.isValidChain(), "fix previous hash reference");

        String originalData = (String) ((Block)blockchain.getChain().get(3)).getData();
        ((Block)blockchain.getChain().get(3)).setData("it's not a present from our God.");
        assertFalse(blockchain.isValidChain(), "chain contains a block wih an invalid field");
        ((Block)blockchain.getChain().get(3)).setData(originalData);
        assertTrue(blockchain.isValidChain(), "chain does not contain any invalid blocks");
    }

    @Test
    void testMineBlock() {
        Block lastBlock = Block.genesis();
        String data = "mined data";
        Block minedBlock = blockchain.mineBlock(lastBlock, data);
        String generatedHash = CryptoHash.calculateHash(minedBlock.getTimestamp(), lastBlock.getHash(), data);

        assertTrue(minedBlock instanceof Block, "returns a Block instance");
        assertEquals(minedBlock.getPreviousHash(), lastBlock.getHash(),
                "sets the `lastHash` to be the `hash` of the lastBlock");
        assertEquals(minedBlock.getData(), data, "sets the `data`");
        assertNotNull(minedBlock.getTimestamp(), "sets a `timestamp`");
        assertEquals(minedBlock.getHash(), generatedHash, "creates a SHA-256 `hash` based on the proper inputs");

        logToConsole(minedBlock, "mined block");
    }

    @Test //initial concept validation
    void outputSimpleChain() {
        Block genesisBlock = Block.genesis();
        System.out.println("Hash for block 1 : " + genesisBlock.getHash());

        Block secondBlock = blockchain.mineBlock(genesisBlock, "Yo im the second block");
        System.out.println("Hash for block 2 : " + secondBlock.getHash());

        Block thirdBlock = blockchain.mineBlock(secondBlock, "Hey im the third block");
        System.out.println("Hash for block 3 : " + thirdBlock.getHash());

        blockchain.addBlock("Yo im the second block");
        blockchain.addBlock("Hey im the third block");

        System.out.print("\nValid: " + blockchain.isValidChain());
        logToConsole(blockchain, "Simple-Chain|OUTPUT");
    }

}
