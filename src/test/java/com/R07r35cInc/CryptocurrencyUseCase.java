package com.R07r35cInc;

import com.BaseTest;
import com.R07r35cInc.blockchain.Block;
import com.R07r35cInc.util.CryptoHash;
import com.R07r35cInc.blockchain.NetChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
public class CryptocurrencyUseCase extends BaseTest {

    NetChain<List<MockTransaction>> netChain;
    List<MockTransaction> listOfTransactions;

    @BeforeEach
    void init() {
        netChain = new NetChain<List<MockTransaction>>();
        listOfTransactions = new ArrayList<MockTransaction>();
        for (int i=0; i<6; i++) {
            HashMap<String, BigDecimal> outputMap = new HashMap<String,BigDecimal>();
            outputMap.put("Transaction"+i, new BigDecimal(i));
            outputMap.put("Transaction"+i+1, new BigDecimal(i+i));
            listOfTransactions.add(new MockTransaction(outputMap));
        }
        netChain.addBlock(listOfTransactions);
    }

    @Test
    void typedDataObjectList() {
        Block lastBlock = (Block) netChain.getChain().get(netChain.getChain().size()-1);
        String validatedHash = CryptoHash.calculateHash(lastBlock);
        assertEquals(((Block<?>) netChain.getChain().get(netChain.getChain().size()-1)).getHash(), validatedHash);
    }

    @Test
    void dataObjectListChangedOrder() {
        Object someObjectInTheMiddle = listOfTransactions.get(2);
        listOfTransactions.remove(2);
        listOfTransactions.add((MockTransaction) someObjectInTheMiddle);
        assertTrue(listOfTransactions.indexOf(someObjectInTheMiddle)==5);

        Block lastBlock = (Block) netChain.getChain().get(netChain.getChain().size()-1);
        String validatedHash = CryptoHash.calculateHash(lastBlock);
        assertNotEquals(((Block<?>) netChain.getChain().get(netChain.getChain().size()-1)).getHash(), validatedHash,
                "change in the object list order will not produce the same json parsed value");

        NetChain broadcastChain = new NetChain();
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> broadcastChain.replaceChain(netChain.getChain()),
                "Expected replaceChain() to throw"
        );
        assertTrue(thrown.getMessage().contains("The incoming chain must be valid"),
                "the new chain is valid");
    }

    @Test
    void chainReplacement() {
        NetChain broadcastChain = new NetChain();
        broadcastChain.replaceChain(netChain.getChain());
        assertTrue(broadcastChain.getChain().size()==netChain.getChain().size());
    }
}

class MockTransaction {
    public UUID id;
    public Map<String, BigDecimal> outputMap;

    public MockTransaction(Map<String, BigDecimal> outputMap) {
        this.id = UUID.randomUUID();
        this.outputMap = outputMap;
    }
}
