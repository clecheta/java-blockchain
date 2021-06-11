package com.R07r35cInc;

import com.BaseTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginUseCase extends BaseTest {

    private static final int GENERATE_USERS = 100000;

    static Blockchain blockchain;
    static List<String> userList;

    @BeforeAll
    static void init() {
        blockchain = new Blockchain();
        userList = new ArrayList<String>();
        User user;
        Block addedBLock;
        for (int i=0; i<GENERATE_USERS; i++) {
            user = new User();
            user.username = "user" + i;
            user.password = UUID.randomUUID().toString().substring(0,8);
            blockchain.addBlock(user);

            addedBLock = (Block) blockchain.getChain().get(blockchain.getChain().size()-1);
            userList.add(addedBLock.getHash());
        }
    }

    @Test
    void findUserByHash() {
        Random random = new Random();
        int randomInt;
        String userHash;
        Long userIndex;
        Block actualBlock;
        User actualUser;
        String expectedUser;
        for (int i=0; i<GENERATE_USERS; i++) {
            randomInt = random.nextInt(blockchain.getChain().size()-1);
            userHash = userList.get(randomInt);
            userIndex = (Long) blockchain.map.get(userHash);
            actualBlock = (Block) blockchain.getChain().get(userIndex.intValue());
            actualUser = (User) actualBlock.getData();
            expectedUser = "user" + randomInt;
            assertEquals(userIndex, actualBlock.getIndex(), "index must match");
            assertEquals(expectedUser, actualUser.username, "must find exact user in the chain");
        }
    }

    @Test
    void findUserByUsername() {
        //!there is the concept in cryptochain to consult a transaction by hash, so there must be a way to quick
        // navigate in the chain data records
    }

}

class User {
    public String username;
    public String password;
    public String tenantId;

    @Override
    public String toString() {
        return "User{ " + username + " }";
    }
}
