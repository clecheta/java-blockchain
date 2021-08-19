package com.R07r35cInc.blockchain;

import com.BaseTest;
import com.R07r35cInc.blockchain.Block;
import com.R07r35cInc.blockchain.NetChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class NetChainTest extends BaseTest {

    private static final int GENERATE_BLOCKS = 10;

    NetChain netChain;
    Block block;

    @BeforeEach
    void init() {
        netChain = new NetChain();
    }

    @Test
    void testNonce() {
        Date date;
        Duration diff;
        for (int i = 1; i <= GENERATE_BLOCKS; i++) {
            date = new Date();
            netChain.addBlock("data" + i);
            block = (Block) netChain.getChain().get(i);
            diff = Duration.between(date.toInstant(), block.getDate().toInstant());
            //we can assert this >=2 if the nounceKey is at least 4 zeros (0000)
            //assertTrue(diff.toMillis() >= 0);
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

    @Test
    void testGsonSerializeDeserialize() {
        //TODO
    }

}
