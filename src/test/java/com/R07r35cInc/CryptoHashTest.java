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

        String expectedHash = CryptoHash.generateHash("aValue" + "another");
        String actualHash = CryptoHash.generateHash("aValue" + "another");
        assertEquals(expectedHash, actualHash, "produces the same hash with the same input arguments");
    }


    //TODO: test the other methods of this utilitary class

}
