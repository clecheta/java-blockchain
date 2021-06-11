package com.R07r35cInc;

import com.BaseTest;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CryptoHashTest extends BaseTest {

    @Test
    void generateHash() {
        assertEquals("077af691b59e1e08dbf12c5cb4b83640600c8a11e67a5919d7f2daa7494b1d08",
                CryptoHash.generateHash("JesusLives"),
                "generates a SHA-256 hashed output");

        String expectedHash = CryptoHash.generateHash("aValue" + "another");
        String actualHash = CryptoHash.generateHash("aValue" + "another");
        assertEquals(expectedHash, actualHash, "produces the same hash with the same input arguments");
    }


    @Test
    void changeMethodsSameResult() {
        Block aBlock = new Block(
                1000L,
                "previous-hash",
                "hash",
                "a-block");

        String serializedData = new GsonBuilder().setPrettyPrinting().create().toJson(aBlock.getData());
        String generatedHash = CryptoHash.generateHash(
                aBlock.getTimestamp() +
                        aBlock.getPreviousHash() +
                        serializedData);

        String calculatedHashBlock = CryptoHash.calculateHash(aBlock);

        String calculatedHashArguments = CryptoHash.calculateHash(
                aBlock.getTimestamp(),
                aBlock.getPreviousHash(),
                aBlock.getData());

        assertEquals(calculatedHashBlock, generatedHash);
        assertEquals(calculatedHashArguments, generatedHash);
        assertEquals(calculatedHashBlock, calculatedHashArguments);
    }

}
