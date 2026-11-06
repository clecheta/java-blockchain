package com.R07r35cInc.blockchain;

import com.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Incremental peer sync: append one validated tip block.
 *
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
public class AppendValidatedBlockTest extends BaseTest {

    BlockChain<String> chain;
    BlockChain<String> peer;

    @BeforeEach
    void init() {
        chain = new BlockChain<>();
        peer = new BlockChain<>();
    }

    @Test
    void appendsWhenTipMatches() {
        Block<String> mined = chain.addBlock("Book of Genesis");
        assertDoesNotThrow(() -> peer.appendValidatedBlock(mined));
        assertEquals(2, peer.getChain().size());
        assertEquals(mined.getHash(), peer.getChain().get(1).getHash());
    }

    @Test
    void rejectsWrongPreviousHash() {
        Block<String> mined = chain.addBlock("Book of Exodus");
        mined.setPreviousHash("broken-previous-hash");
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> peer.appendValidatedBlock(mined));
        assertTrue(thrown.getMessage().contains("previousHash"));
    }

    @Test
    void rejectsWrongNumber() {
        Block<String> mined = chain.addBlock("Book of Leviticus");
        mined.setNumber(99L);
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> peer.appendValidatedBlock(mined));
        assertTrue(thrown.getMessage().contains("tip+1"));
    }

    @Test
    void rejectsTamperedHash() {
        Block<String> mined = chain.addBlock("Book of Numbers");
        mined.setHash("tampered-hash");
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> peer.appendValidatedBlock(mined));
        assertTrue(thrown.getMessage().contains("hash is invalid"));
    }

    @Test
    void rejectsNullBlock() {
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> peer.appendValidatedBlock(null));
        assertTrue(thrown.getMessage().contains("must not be null"));
    }
}
