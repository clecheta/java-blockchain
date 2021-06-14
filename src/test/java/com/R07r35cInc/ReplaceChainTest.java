package com.R07r35cInc;

import com.BaseTest;
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
                () -> simpleChain.replaceChain(newChain.getChain()),
                "Expected replaceChain() to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("The incoming chain must be longer"),
                "the new chain is not longer");
    }

    @Test
    void longerButInvalidChain() {
        minedBlock = newChain.addBlock("additional block");
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> ((Block) newChain.getChain().get(1)).setPreviousHash("some-fake-hash")
        );
        assertTrue(thrown.getMessage().contains("please dont try to change the previous hash"));

        thrown = assertThrows(
                RuntimeException.class,
                () -> simpleChain.replaceChain(newChain.getChain()),
                "Expected replaceChain() to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("The incoming chain must be valid"),
                "the new chain is invalid");
    }

    @Test
    void chainIsValid() {
        minedBlock = newChain.addBlock("Book of Exodus");
        assertDoesNotThrow(() -> {
            simpleChain.replaceChain(newChain.getChain());
        });
    }

}
