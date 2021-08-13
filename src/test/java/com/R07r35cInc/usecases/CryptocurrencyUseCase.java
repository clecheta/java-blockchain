package com.R07r35cInc.usecases;

import com.BaseTest;
import com.R07r35cInc.Block;
import com.R07r35cInc.CryptoHash;
import com.R07r35cInc.NetChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CryptocurrencyUseCase extends BaseTest {

    NetChain netChain;
    List<Map<String, BigDecimal>> listOfTransactions;

    @BeforeEach
    void init() {
        netChain = new NetChain();
        listOfTransactions = new ArrayList<Map<String,BigDecimal>>();
        for (int i=0; i<6; i++) {
            HashMap<String, BigDecimal> transactions = new HashMap<String,BigDecimal>();
            transactions.put("Transaction"+i, new BigDecimal(i));
            transactions.put("Transaction"+i+1, new BigDecimal(i+i));
            listOfTransactions.add(transactions);
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
        Map<String, BigDecimal> someObjectInTheMiddle = listOfTransactions.get(2);
        listOfTransactions.remove(2);
        listOfTransactions.add(someObjectInTheMiddle);
        assertTrue(listOfTransactions.indexOf(someObjectInTheMiddle)==5);

        Block lastBlock = (Block) netChain.getChain().get(netChain.getChain().size()-1);
        String validatedHash = CryptoHash.calculateHash(lastBlock);
        assertNotEquals(((Block<?>) netChain.getChain().get(netChain.getChain().size()-1)).getHash(), validatedHash,
                "change in the object list order will not produce the same json parsed value");
    }

    @Test
    void chainReplacement() {
        NetChain broadcastChain = new NetChain();
        broadcastChain.replaceChain(netChain.getChain());
        assertTrue(broadcastChain.getChain().size()==netChain.getChain().size());
    }

}
