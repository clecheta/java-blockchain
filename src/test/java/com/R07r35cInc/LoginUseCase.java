package com.R07r35cInc;

import com.BaseTest;
import com.R07r35cInc.blockchain.Block;
import com.R07r35cInc.blockchain.BlockChain;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
public class LoginUseCase extends BaseTest {

    private static final int GENERATE_USERS = 100000;

    static BlockChain blockChain;
    static List<String> userList;
    static Map<String, Long> map = new HashMap<String, Long>();

    @BeforeAll
    static void init() {
        blockChain = new BlockChain();
        userList = new ArrayList<String>();
        User user;
        Block addedBLock;
        for (int i=0; i<GENERATE_USERS; i++) {
            user = new User();
            user.username = "user" + i;
            user.password = UUID.randomUUID().toString().substring(0,8);
            blockChain.addBlock(user);

            addedBLock = (Block) blockChain.getChain().get(blockChain.getChain().size()-1);
            userList.add(addedBLock.getHash());
            map.put(addedBLock.getHash(), blockChain.getChain().size()-1L);
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
            randomInt = random.nextInt(blockChain.getChain().size()-1);
            userHash = userList.get(randomInt);
            userIndex = (Long) map.get(userHash);
            actualBlock = (Block) blockChain.getChain().get(userIndex.intValue());
            actualUser = (User) actualBlock.getData();
            expectedUser = "user" + randomInt;
            assertEquals(userIndex, actualBlock.getNumber(), "index must match");
            assertEquals(expectedUser, actualUser.username, "must find exact user in the chain");
            assertEquals("88169f6e-f67c-4835-873e-b5f0d7711c65", actualUser.tenantId);
        }
    }

    @Test
    void findUserByUsername() {
        /*
         Storing Blocks in PostgreSQL after they were mined/added to the chain is one approach, it allows me to:
         - find user by hash or e-mail directly in the database, use the block index to validated is the
         just loaded block is equal the ones in the chain
         - load the list of blocks findAll() into a blockchain by using the replaceChain() method, and thus validate
         the whole block list.

         to use this approach we might return Block in addBlock() function of BlockChain
        */
    }

}

class User {
    public String username;
    public String password;
    public String tenantId = "88169f6e-f67c-4835-873e-b5f0d7711c65";

    @Override
    public String toString() {
        return "User{ " + username + " }";
    }
}
