package com.R07r35cInc;


import com.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NetChainTest extends BaseTest {

    private static final int GENERATE_BLOCKS = 100;

    NetChain netChain;

    @BeforeEach
    void init() {
        netChain = new NetChain();
    }

    @Test
    void testNonce() {
        for (int i = 1; i <= GENERATE_BLOCKS; i++) {
            netChain.addBlock("data" + i);
        }
        assertEquals(netChain.getChain().size(), GENERATE_BLOCKS + 1, "chain size must match");

        Block block;
        for (int i = 1; i <= GENERATE_BLOCKS; i++) {
            block = (Block) netChain.getChain().get(i);
            assertEquals(i, block.getIndex(), "get the right block");
            assertTrue(block.getNonce() > 0, "nonce must be calculated");
        }
        logToConsole(netChain.getChain(), "netChain");
    }

}
