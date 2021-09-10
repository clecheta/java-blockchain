package com.R07r35cInc.blockchain;

import com.BaseTest;
import com.R07r35cInc.blockchain.Block;
import com.R07r35cInc.blockchain.SimpleChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReplaceChainTest extends BaseTest {

    SimpleChain simpleChain;
    SimpleChain newChain;
    List<Block> originalChain;
    Block minedBlock; //just to satisfy method return;

    @BeforeEach
    void init() {
        simpleChain = new SimpleChain();
        newChain = new SimpleChain();
        originalChain = simpleChain.getChain();
    }

    @Test
    void notLongerChain() {
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> simpleChain.replaceChain(newChain.getChain(), false),
                "Expected replaceChain() to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("The incoming chain must be longer"),
                "the new chain is not longer");
    }

    @Test
    void longerButInvalidChain() {
        minedBlock = newChain.addBlock("additional block");
        ((Block) newChain.getChain().get(1)).setPreviousHash("some-fake-hash");

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> simpleChain.replaceChain(newChain.getChain(), false),
                "Expected replaceChain() to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("The incoming chain must be valid"),
                "the new chain is invalid");
    }

    @Test
    void chainIsValid() {
        minedBlock = newChain.addBlock("Book of Exodus");
        assertDoesNotThrow(() -> {
            simpleChain.replaceChain(newChain.getChain(), false);
        });
    }

    @Test
    void validateTransactions() {
        newChain.addBlock("foo"); //need a bigger chain then original to call replaceChain()
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> simpleChain.replaceChain(newChain.getChain(), true),
                "the validateTransactions flag is true"
        );
        assertTrue(thrown.getMessage().contains("The incoming chain has invalid data"),
                "the new chain has invalid data");
    }

}
