package com.R07r35cInc;

import com.BaseTest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class BlockTest extends BaseTest {

    @Test
    void testFields() {
        Field[] fields = Block.class.getDeclaredFields();
        assertTrue(fields.length >= 4, "class fields violated");
        assertEquals(true, Arrays.stream(fields).anyMatch(f -> f.getName().equals("previousHash")));
        assertEquals(true, Arrays.stream(fields).anyMatch(f -> f.getName().equals("hash")));
        assertEquals(true, Arrays.stream(fields).anyMatch(f -> f.getName().equals("data")));
        for (Field f : fields) {
            if (f.getName().equals("hash")) {
                assertEquals(0, f.toGenericString().indexOf("private"));
                assertEquals(true, f.toGenericString().contains("String"));
            } else if (f.getName().equals("date")) {
                assertEquals(0, f.toGenericString().indexOf("private"));
                assertEquals(true, f.toGenericString().contains("Date"));
            } else if (f.getName().equals("index")) { //necessary Object class to json deserialization
                assertEquals(0, f.toGenericString().indexOf("private"));
                assertEquals(true, f.toGenericString().contains("Long"));
            } else if (f.getName().equals("nonce")) { //necessary Object class to json deserialization
                assertEquals(0, f.toGenericString().indexOf("private"));
                assertEquals(true, f.toGenericString().contains("Long"));
            }
        }
    }

    @Test
    void instantiate() {
        Block genesisBlock = Block.genesis();
        assertNotNull(genesisBlock);
        assertTrue(genesisBlock instanceof Block);
        assertEquals("Yes-I-am-coming-soon.Amen.Come-Lord-Jesus", genesisBlock.getPreviousHash());
        assertEquals("The-stone-the-builders-rejected-has-become-the-cornerstone", genesisBlock.getHash());
        assertEquals("Book of Genesis", genesisBlock.getData());
        logToConsole(genesisBlock, null);
    }

    @Test
    void addDomainModelAsData() {
        User user = new User();
        user.username = "user1";
        user.password = "pass1";

        Block userBlock = new Block<User>(
                new Date(),
                "The-stone-the-builders-rejected-has-become-the-cornerstone",
                "A-user-hash",
                user, 0L, 0L);
        assertNotNull(userBlock);
        assertTrue(userBlock.getData() instanceof User);
        logToConsole(userBlock, "user data model (value object) block example");
    }

    @Test
    void testEqualsAndHashcode() {
        Block block1 = Block.genesis();
        Block block2 = new Block(
                block1.getDate(),
                block1.getPreviousHash(),
                block1.getHash(),
                block1.getData(),
                0L, 0L);
        assertEquals(block1, block2);
        assertFalse(block1 == block2, "ensure its not the same memory object reference");
        assertTrue(block1.hashCode() == block2.hashCode());
    }

    @Test
    void testGenesisClone() {
        Block genesisBlock = Block.genesis();
        Block sameFirstBlock = new Block(
                genesisBlock.getDate(),
                genesisBlock.getPreviousHash(),
                genesisBlock.getHash(),
                genesisBlock.getData(),
                0L,
                0L
        );
        assertEquals(genesisBlock, sameFirstBlock, "genesis block clones must match genesis data");
        //extra validation for the fields outside equals and hashCode
        assertEquals(genesisBlock.getIndex(), 0L, "genesis block clones must match index data");
        assertEquals(genesisBlock.getNonce(), 0L, "genesis block clones must match nonce data");
    }

    class User {
        public String username;
        public String password;
    }

}
