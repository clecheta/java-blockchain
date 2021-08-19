package com.R07r35cInc.blockchain;

import com.BaseTest;
import com.R07r35cInc.blockchain.Block;
import com.R07r35cInc.blockchain.SimpleChain;
import com.R07r35cInc.util.CryptoHash;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleChainTest extends BaseTest {

    SimpleChain simpleChain;
    Block minedBlock; //just to satisfy method return;

    @BeforeEach
    void init() {
        simpleChain = new SimpleChain();
    }

    @Test
    void testMethods() {
        Method[] methods = SimpleChain.class.getDeclaredMethods();
        assertEquals(true, Arrays.stream(methods).anyMatch(m -> m.getName().equals("isValidChain")));
        for (Method m : methods) {
            if (m.getName().equals("replaceChain")) {
                assertEquals(0, m.toGenericString().indexOf("public"));
                assertEquals(true, m.toGenericString().contains("void"));
            }
        }
    }

    @Test
    void testChain() {
        assertTrue(simpleChain.getChain() instanceof ArrayList, "contains a `chain` List instance");
        Block actualBlock = (Block) simpleChain.getChain().get(0);
        Block expectedBlock = Block.genesis();
        assertEquals(expectedBlock, actualBlock, "starts with the genesis block");
        BigDecimal newData = BigDecimal.TEN;
        minedBlock = simpleChain.addBlock(newData);
        Block lastBlock = (Block) simpleChain.getChain().get(simpleChain.getChain().size() - 1);
        assertEquals(newData, lastBlock.getData(), "adds a new block to the chain");
    }

    @Test
    void fakeGenesisBlock() {
        ((Block) simpleChain.getChain().get(0)).setData("fake-genesis");
        assertFalse(simpleChain.isValidChain(), "the chain does not start with the genesis block");
    }

    @Test
    void isValidChain() {
        simpleChain.addBlock("Exodus");
        simpleChain.addBlock("Leviticus");
        simpleChain.addBlock("A LEGO GIFT FROM GOD!");

        String originalHash = ((Block) simpleChain.getChain().get(2)).getPreviousHash();
        ((Block) simpleChain.getChain().get(2)).setPreviousHash("broken-previous-hash");
        assertFalse(simpleChain.isValidChain(), "previous hash reference has changed");

        ((Block) simpleChain.getChain().get(2)).setPreviousHash(originalHash);
        assertTrue(simpleChain.isValidChain(), "fix previous hash reference");

        String originalData = (String) ((Block) simpleChain.getChain().get(3)).getData();
        ((Block) simpleChain.getChain().get(3)).setData("it's not a present from our God.");
        assertFalse(simpleChain.isValidChain(), "chain contains a block wih an invalid field");

        ((Block) simpleChain.getChain().get(3)).setData(originalData);
        assertTrue(simpleChain.isValidChain(), "chain does not contain any invalid blocks");
    }

    @Test
    void testMineBlock() {
        Block lastBlock = Block.genesis();
        String data = "mined data";
        minedBlock = simpleChain.mineBlock(lastBlock, data);
        String calculatedHash = CryptoHash.calculateHash(minedBlock.getDate(),
                lastBlock.getHash(), data, minedBlock.getIndex(), minedBlock.getNonce());

        assertTrue(minedBlock instanceof Block, "returns a Block instance");
        assertEquals(minedBlock.getPreviousHash(), lastBlock.getHash(),
                "sets the `lastHash` to be the `hash` of the lastBlock");
        assertEquals(minedBlock.getData(), data, "sets the `data`");
        assertNotNull(minedBlock.getDate(), "sets a `timestamp`");
        assertEquals(minedBlock.getHash(), calculatedHash, "creates a SHA-256 `hash` based on the proper inputs");

        logToConsole(minedBlock, "mined block");
    }

    @Test
        //initial concept validation
    void outputSimpleChain() {
        Block genesisBlock = Block.genesis();
        System.out.println("Hash for block 1 : " + genesisBlock.getHash());

        Block secondBlock = simpleChain.mineBlock(genesisBlock, "Book of Exodus");
        System.out.println("Hash for block 2 : " + secondBlock.getHash());

        Block thirdBlock = simpleChain.mineBlock(secondBlock, "Book of Leviticus");
        System.out.println("Hash for block 3 : " + thirdBlock.getHash());

        minedBlock = simpleChain.addBlock("Book of Numbers");
        minedBlock = simpleChain.addBlock("Book of Deuteronomy");

        System.out.print("\nValid: " + simpleChain.isValidChain());
        logToConsole(simpleChain, "Simple-Chain|OUTPUT");
    }

}
