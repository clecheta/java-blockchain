package com.R07r35cInc;

import com.BaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CryptoHashTest extends BaseTest {

    @Test
    public void generateHash() {
        assertEquals("077af691b59e1e08dbf12c5cb4b83640600c8a11e67a5919d7f2daa7494b1d08",
                CryptoHash.generateHash("JesusLives"),
                "generates a SHA-256 hashed output");
    }

    /*
    @Test
    public void testCalculateHash() {
        Long timestamp = new Date().getTime();
        String hash1 = CryptoHash.generateHash(timestamp + "A" + "B");
        String hash2 = CryptoHash.generateHash("B" + "A" + timestamp);
        assertEquals(hash1, hash2, "produces the same hash with the same input arguments in any order");
    }*/

}
