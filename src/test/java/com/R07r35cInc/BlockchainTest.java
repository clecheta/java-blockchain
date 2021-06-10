package com.R07r35cInc;


import com.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BlockchainTest extends BaseTest {

    Blockchain blockchain;

    @BeforeEach
    void init() {
        blockchain = new Blockchain();
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

        logToConsole(minedBlock);
    }

    @Test
    void testSimpleChain() {
        Block genesisBlock = Block.genesis();
        System.out.println("Hash for block 1 : " + genesisBlock.getHash());

        Block secondBlock = blockchain.mineBlock(genesisBlock, "Yo im the second block");
        System.out.println("Hash for block 2 : " + secondBlock.getHash());

        Block thirdBlock = blockchain.mineBlock(secondBlock, "Hey im the third block");
        System.out.println("Hash for block 3 : " + thirdBlock.getHash());

        //System.out.println("\nValid: " + blockchain.isValidChain());
        logToConsole(blockchain);
    }
}
