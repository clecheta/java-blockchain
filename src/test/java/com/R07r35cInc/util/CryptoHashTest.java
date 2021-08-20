package com.R07r35cInc.util;

import com.BaseTest;
import com.R07r35cInc.blockchain.Block;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CryptoHashTest extends BaseTest {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(CryptoHash.timestampFormat);

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
        long index = 0L;
        long nonce = 0L;
        Block aBlock = new Block(new Date(),
                "previous-hash", "hash",
                "a-block", index, nonce);

        String serializedData = new Parse<>().toJson(aBlock.getData());
        String timestamp  = dateFormat.format(aBlock.getDate());
        String generatedHash = CryptoHash.generateHash(timestamp +
                        aBlock.getPreviousHash() + serializedData + index + nonce);

        String calculatedHashBlock = CryptoHash.calculateHash(aBlock);

        String calculatedHashArguments = CryptoHash.calculateHash(
                aBlock.getDate(),
                aBlock.getPreviousHash(),
                aBlock.getData(),
                aBlock.getIndex(),
                aBlock.getNonce());

        assertEquals(calculatedHashBlock, generatedHash);
        assertEquals(calculatedHashArguments, generatedHash);
        assertEquals(calculatedHashBlock, calculatedHashArguments);
    }

}
