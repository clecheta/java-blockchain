package com.R07r35cInc.blockchain;

import com.BaseTest;
import com.R07r35cInc.util.CryptoHash;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
public class BlockChainTest extends BaseTest {

    BlockChain blockChain;
    Block minedBlock; //just to satisfy method return;

    @BeforeEach
    void init() {
        blockChain = new BlockChain();
    }

    @Test
    void testMethods() {
        Method[] methods = BlockChain.class.getDeclaredMethods();
        assertEquals(true, Arrays.stream(methods).anyMatch(m -> m.getName().equals("isValidChain")));
        for (Method m : methods) {
            if (m.getName().equals("replaceChain")) {
                assertEquals(0, m.toGenericString().indexOf("public"));
                assertEquals(true, m.toGenericString().contains("Boolean"));
            }
        }
    }

    @Test
    void testChain() {
        assertTrue(blockChain.getChain() instanceof ArrayList, "contains a `chain` List instance");
        Block actualBlock = (Block) blockChain.getChain().get(0);
        Block expectedBlock = Block.genesis();
        assertEquals(expectedBlock, actualBlock, "starts with the genesis block");
        BigDecimal newData = BigDecimal.TEN;
        minedBlock = blockChain.addBlock(newData);
        Block lastBlock = (Block) blockChain.getChain().get(blockChain.getChain().size() - 1);
        assertEquals(newData, lastBlock.getData(), "adds a new block to the chain");
    }

    @Test
    void fakeGenesisBlock() {
        ((Block) blockChain.getChain().get(0)).setData("fake-genesis");
        assertFalse(blockChain.isValidChain(), "the chain does not start with the genesis block");
    }

    @Test
    void isValidChain() {
        blockChain.addBlock("Exodus");
        blockChain.addBlock("Leviticus");
        blockChain.addBlock("A LEGO GIFT FROM GOD!");

        String originalHash = ((Block) blockChain.getChain().get(2)).getPreviousHash();
        ((Block) blockChain.getChain().get(2)).setPreviousHash("broken-previous-hash");
        assertFalse(blockChain.isValidChain(), "previous hash reference has changed");

        ((Block) blockChain.getChain().get(2)).setPreviousHash(originalHash);
        assertTrue(blockChain.isValidChain(), "fix previous hash reference");

        String originalData = (String) ((Block) blockChain.getChain().get(3)).getData();
        ((Block) blockChain.getChain().get(3)).setData("it's not a present from our God.");
        assertFalse(blockChain.isValidChain(), "chain contains a block wih an invalid field");

        ((Block) blockChain.getChain().get(3)).setData(originalData);
        assertTrue(blockChain.isValidChain(), "chain does not contain any invalid blocks");
    }

    @Test
    void testMineBlock() {
        Block lastBlock = Block.genesis();
        String data = "mined data";
        minedBlock = blockChain.mineBlock(lastBlock, data);
        String calculatedHash = CryptoHash.calculateHash(minedBlock.getDate(),
                lastBlock.getHash(), data, minedBlock.getNumber(), minedBlock.getNonce());

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

        Block secondBlock = blockChain.mineBlock(genesisBlock, "Book of Exodus");
        System.out.println("Hash for block 2 : " + secondBlock.getHash());

        Block thirdBlock = blockChain.mineBlock(secondBlock, "Book of Leviticus");
        System.out.println("Hash for block 3 : " + thirdBlock.getHash());

        minedBlock = blockChain.addBlock("Book of Numbers");
        minedBlock = blockChain.addBlock("Book of Deuteronomy");

        System.out.print("\nValid: " + blockChain.isValidChain());
        logToConsole(blockChain, "Simple-Chain|OUTPUT");
    }

}
